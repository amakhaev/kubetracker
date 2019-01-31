package com.tracker.domain.pods;

import com.tracker.domain.filter.FilterModel;
import com.tracker.domain.filter.FilterService;
import com.tracker.domain.filter.FilterType;
import com.tracker.domain.token.TokenModel;
import com.tracker.domain.token.TokenService;
import io.fabric8.kubernetes.api.model.ContainerState;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the implementation of {@link PodListService}
 */
@Slf4j
@Service
class PodListServiceImpl implements PodListService {


    private final String kubeMasterUrl;
    private final TokenService tokenService;
    private final FilterService filterService;

    /**
     * Initialize new instance of {@link PodListServiceImpl}
     */
    @Autowired
    PodListServiceImpl(TokenService tokenService,
                       FilterService filterService,
                       @Value("${kube.master_url}") String masterUrl) {
        this.kubeMasterUrl = masterUrl;
        this.tokenService = tokenService;
        this.filterService = filterService;
    }

    /**
     * Gets the podList by given namespace
     *
     * @param namespace    - the namespace of kubernetes cluster
     * @param applyFilters - indicates when filters should be applied to result list
     * @return the {@link List < PodModel >} podList
     */
    @Override
    public List<PodModel> getPods(String namespace, boolean applyFilters) {
        TokenModel tokenModel = this.tokenService.getToken();

        if (tokenModel.getAccessToken() == null) {
            throw new NullPointerException("The access token is empty");
        }

        Config kubeClientConfig = new ConfigBuilder()
                .withMasterUrl(this.kubeMasterUrl)
                .withTrustCerts(true)
                .withNamespace(namespace)
                .build();

        kubeClientConfig.setOauthToken(tokenModel.getAccessToken());
        final KubernetesClient client = new DefaultKubernetesClient(kubeClientConfig);
        PodList podList = client.pods().list();

        if (podList == null || podList.getItems() == null) {
            return new ArrayList<>();
        }

        List<PodModel> pods = podList.getItems()
                .stream()
                .map(pod -> {
                    PodModel podModel = new PodModel();
                    podModel.setName(pod.getMetadata().getName());
                    podModel.setNamespace(pod.getMetadata().getNamespace());
                    podModel.setStatus(this.getStatusValue(pod));
                    podModel.setRestartCount(this.getRestartCount(pod));
                    podModel.setStartedAt(this.getStartTime(pod));
                    podModel.setReady(this.isReady(pod));

                    return podModel;
                })
                .sorted(Comparator.comparing(PodModel::getName))
                .collect(Collectors.toList());

        return applyFilters ? this.applyFilters(pods) : pods;
    }

    private String getStatusValue(Pod pod) {
        if (pod.getStatus() == null ||
                pod.getStatus().getContainerStatuses() == null ||
                pod.getStatus().getContainerStatuses().isEmpty() ||
                pod.getStatus().getContainerStatuses().get(0).getState() == null) {
            return "Unknown";
        }

        ContainerState state = pod.getStatus().getContainerStatuses().get(0).getState();
        if (state.getRunning() != null) {
            return "Running";
        } else if (state.getWaiting() != null) {
            return state.getWaiting().getReason();
        } else if (state.getTerminated() != null) {
            return state.getTerminated().getReason();
        } else {
            return "Unknown";
        }
    }

    private Integer getRestartCount(Pod pod) {
        if (pod.getStatus() == null ||
                pod.getStatus().getContainerStatuses() == null ||
                pod.getStatus().getContainerStatuses().isEmpty()) {
            return null;
        }

        return pod.getStatus().getContainerStatuses().get(0).getRestartCount();
    }

    private String getStartTime(Pod pod) {
        if (pod.getStatus() == null) {
            return null;
        }

        return pod.getStatus().getStartTime();
    }

    private boolean isReady(Pod pod) {
        if (pod.getStatus() == null ||
                pod.getStatus().getContainerStatuses() == null ||
                pod.getStatus().getContainerStatuses().isEmpty()) {
            return false;
        }

        ContainerStatus containerStatus = pod.getStatus().getContainerStatuses().get(0);
        return containerStatus.getReady();

    }

    private List<PodModel> applyFilters(List<PodModel> availablePods) {
        if (availablePods == null || availablePods.isEmpty()) {
            return availablePods;
        }

        List<FilterModel> filters = this.filterService.getFilters(FilterType.POD);
        if (filters == null || filters.isEmpty()) {
            return availablePods;
        }

        return availablePods.stream()
                .filter(pod -> filters.stream().anyMatch(
                        filter -> {
                            String filterValue = filter.getFilterValue().toLowerCase();
                            String podName = pod.getName().toLowerCase();
                            return podName.startsWith(filterValue);
                        })
                )
                .collect(Collectors.toList());
    }
}
