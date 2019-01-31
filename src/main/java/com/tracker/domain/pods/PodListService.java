package com.tracker.domain.pods;

import java.util.List;

/**
 * Provides the service to work with PODs
 */
public interface PodListService {

    /**
     * Gets the podList by given namespace
     *
     * @param namespace - the namespace of kubernetes cluster
     * @param applyFilters - indicates when filters should be applied to result list
     * @return the {@link List<PodModel>} podList
     */
    List<PodModel> getPods(String namespace, boolean applyFilters);
}
