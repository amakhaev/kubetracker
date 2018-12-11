package com.tracker.ui.podsTab;

import com.tracker.domain.podFilter.PodFilterModel;
import com.tracker.domain.podFilter.PodFilterService;
import com.tracker.domain.token.TokenService;
import com.tracker.kube.PodsService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Provides the pods pane controller
 */
@Slf4j
class PodsPanelController {

    private PodsService podsService;
    private TokenService tokenService;
    private PodFilterService podFilterService;

    /**
     * Initialize new instance of {@link PodsPanelController}
     */
    PodsPanelController() {
        this.podsService = PodsService.INSTANCE;
        this.tokenService = TokenService.INSTANCE;
        this.podFilterService = PodFilterService.INSTANCE;
    }

    /**
     * Schedules the refreshing of information about pods
     *
     * @return the {@link Observable} instance
     */
    Observable<PodList> scheduleRefreshPods() {
        return Observable.interval(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map((data) -> this.retrievePods());
    }

    /**
     * Gets the data about pods
     */
    Observable<PodList> getPods() {
        return Observable.just(this.retrievePods()).subscribeOn(Schedulers.newThread());
    }

    private PodList retrievePods() {
        log.info("Retrieve pods");
        try {
            PodList podList = this.podsService.getPods(this.tokenService.getToken());
            podList.setItems(this.applyFilters(podList.getItems()));
            log.info("Pods successfully retrieved. Count: " + podList.getItems().size());
            return podList;
        } catch (Exception e) {
            log.error("Can't retrieve pods: " + e.getMessage());
            return new PodList();
        }
    }

    private List<Pod> applyFilters(List<Pod> pods) {
        if (pods == null || pods.isEmpty()) {
            return pods;
        }

        List<PodFilterModel> filters = this.podFilterService.getFilters();
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
