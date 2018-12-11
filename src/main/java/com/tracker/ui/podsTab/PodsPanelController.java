package com.tracker.ui.podsTab;

import com.tracker.domain.token.TokenService;
import com.tracker.kube.PodsService;
import io.fabric8.kubernetes.api.model.PodList;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Provides the pods pane controller
 */
@Slf4j
class PodsPanelController {

    private PodsService podsService;
    private TokenService tokenService;

    /**
     * Initialize new instance of {@link PodsPanelController}
     */
    PodsPanelController() {
        this.podsService = PodsService.INSTANCE;
        this.tokenService = TokenService.INSTANCE;
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
        log.info("Retrieve pods");
        try {
            PodList podList = this.podsService.getPods(this.tokenService.getToken());
            log.info("Pods successfully retrieved. Count: " + podList.getItems().size());
            return podList;
        } catch (Exception e) {
            log.error("Can't retrieve pods: " + e.getMessage());
            return new PodList();
        }
    }
}
