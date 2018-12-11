package com.tracker.ui.controls.multiselect;

import java.util.List;

/**
 * Provides the data source for multiselect items
 */
public interface MultiSelectDataSource {

    /**
     * Creates the filter
     *
     * @param filter - the filters to create
     */
    void create(String filter);

    /**
     * Deletes the filter
     *
     * @param id - the identifier of filter to delete
     */
    void delete(int id);

    /**
     * Gets the list of available filters
     *
     * @return the {@link List < MultiSelectDialogItem >} instance
     */
    List<MultiSelectDialogItem> getFilters();

    /**
     * Indicates when filter already exists in database
     *
     * @param filterValue - the value to search
     * @return true when filter with given value already present in database, false otherwise
     */
    boolean isFilterExists(String filterValue);
}
