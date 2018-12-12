package com.tracker.ui.podsTab;

import com.tracker.domain.filter.FilterModel;
import com.tracker.domain.filter.FilterService;
import com.tracker.domain.filter.FilterType;
import com.tracker.ui.controls.multiselect.MultiSelectDataSource;
import com.tracker.ui.controls.multiselect.MultiSelectDialogItem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the adapter to work with pod filters
 */
public class PodFilterAdapter implements MultiSelectDataSource {

    private FilterService filterService;

    /**
     * Initialize new instance of {@link PodFilterAdapter}
     */
    public PodFilterAdapter() {
        this.filterService = FilterService.INSTANCE;
    }

    /**
     * Creates the filter
     *
     * @param filter - the filters to create
     */
    @Override
    public void create(String filter) {
        FilterModel filterModel = new FilterModel();
        filterModel.setFilterValue(filter);
        filterModel.setFilterType(FilterType.POD);
        this.filterService.createOrUpdate(filterModel);
    }

    /**
     * Deletes the filter
     *
     * @param id - the identifier of filter to delete
     */
    @Override
    public void delete(int id) {
        this.filterService.delete(id);
    }

    /**
     * Gets the list of available filters
     *
     * @return the {@link List < MultiSelectDialogItem >} instance
     */
    @Override
    public List<MultiSelectDialogItem> getFilters() {
        return this.filterService.getFilters(FilterType.POD)
                .stream()
                .map(podFilter -> new MultiSelectDialogItem(podFilter.getId(), podFilter.getFilterValue()))
                .collect(Collectors.toList());
    }

    /**
     * Indicates when filter already exists in database
     *
     * @param filterValue - the value to search
     * @return true when filter with given value already present in database, false otherwise
     */
    @Override
    public boolean isFilterExists(String filterValue) {
        return this.filterService.isFilterExists(filterValue, FilterType.POD);
    }
}
