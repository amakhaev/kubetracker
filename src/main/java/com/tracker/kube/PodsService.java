package com.tracker.kube;

import com.tracker.domain.token.TokenModel;
import io.fabric8.kubernetes.api.model.PodList;

/**
 * Provides the service to work with PODs
 */
public interface PodsService {

    /**
     * Provides the base implementation of pods service
     */
    PodsService INSTANCE = new PodsServiceImpl();

    /**
     * Gets the pod list by given token
     *
     * @param tokenModel - the token to retrieve
     * @return the {@link PodList} instance
     */
    PodList getPods(TokenModel tokenModel);
}
