package com.tracker.domain.podFilter;

import java.util.List;

/**
 * Provides the service to work with pods filter
 */
public interface PodFilterService {

    PodFilterService INSTANCE = new PodFilterServiceImpl();

    /**
     * Gets the list of filters for pods
     */
    List<PodFilterModel> getFilters();

    /**
     * Indicates when pod filter already exists in database
     *
     * @param filterValue - the value to search
     * @return true when pod filter with given value already present in database, false otherwise
     */
    boolean isFilterExists(String filterValue);

    /**
     * Creates or updates the filter for pod
     *
     * @param filter - the filter to create or update
     */
    void createOrUpdate(PodFilterModel filter);

    /**
     * Deletes the filter
     *
     * @param id - the identifier of filter to delete
     */
    void delete(int id);


}
