package com.tracker.domain.kube;

import com.tracker.domain.token.TokenModel;
import io.fabric8.kubernetes.api.model.PodList;

/**
 * Provides the service to work with PODs
 */
public interface PodsService {

    /**
     * Gets the base implementation of pods service
     *
     * @param namespace - the namespace where pod stored
     * @return the {@link PodsService} instance
     */
    static PodsService getInstance(String namespace) {
        return new PodsServiceImpl(namespace);
    }

    /**
     * Gets the pod list by given token
     *
     * @param tokenModel - the token to retrieve
     * @return the {@link PodList} instance
     */
    PodList getPods(TokenModel tokenModel);
}
