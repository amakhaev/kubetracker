package com.tracker.ui;

import com.tracker.domain.filter.FilterModel;
import com.tracker.domain.filter.FilterService;
import com.tracker.domain.filter.FilterType;
import com.tracker.domain.token.TokenService;
import com.tracker.domain.kube.PodsService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Provides the pods pane controller
 */
@Slf4j
public class PodsController {

    private static final Map<String, PodsController> instancesPerNamespace = new HashMap<>();

    /**
     * Gets the instance by given namespace
     *
     * @param namespace - the namespace for creating
     * @return the {@link PodsController} instance
     */
    public static PodsController getInstance(String namespace) {
        if (!instancesPerNamespace.containsKey(namespace)) {
            instancesPerNamespace.put(namespace, new PodsController(namespace));
        }

        return instancesPerNamespace.get(namespace);
    }

    private final String namespace;
    private final PodsService podsService;
    private final TokenService tokenService;
    private final FilterService filterService;

    /**
     * Initialize new instance of {@link PodsController}
     */
    private PodsController(String namespace) {
        this.podsService = PodsService.getInstance(namespace);
        this.tokenService = TokenService.INSTANCE;
        this.filterService = FilterService.INSTANCE;
        this.namespace = namespace;
    }

    /**
     * Schedules the refreshing of information about pods
     *
     * @return the {@link Observable} instance
     */
    public Observable<PodList> scheduleRefreshPods() {
        return Observable.interval(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map((data) -> this.retrievePods());
    }

    /**
     * Gets the data about pods
     */
    public Observable<PodList> getPods() {
        return Observable.just(this.retrievePods()).subscribeOn(Schedulers.newThread());
    }

    private PodList retrievePods() {
        log.debug("Namespace: {}, Retrieve pods", this.namespace);
        try {
            PodList podList = this.podsService.getPods(this.tokenService.getToken());
            podList.setItems(this.applyFilters(podList.getItems()));
            log.debug("Namespace: {}, Pods successfully retrieved. Count: {}", this.namespace, podList.getItems().size());
            return podList;
        } catch (Exception e) {
            log.error("Namespace: {},  Can't retrieve pods: {}", this.namespace, e.getMessage());
            return new PodList();
        }
    }

    private List<Pod> applyFilters(List<Pod> pods) {
        if (pods == null || pods.isEmpty()) {
            return pods;
        }

        List<FilterModel> filters = this.filterService.getFilters(FilterType.POD);
        if (filters == null || filters.isEmpty()) {
            return pods;
        }

        return pods.stream()
                .filter(pod -> filters.stream().anyMatch(
                        filter -> {
                            String filterValue = filter.getFilterValue().toLowerCase();
                            String podName = pod.getMetadata().getName().toLowerCase();
                            return podName.startsWith(filterValue);
                        })
                )
                .collect(Collectors.toList());
    }
}
