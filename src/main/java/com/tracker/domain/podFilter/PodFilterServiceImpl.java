package com.tracker.domain.podFilter;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Provides the implementation of {@link PodFilterService}
 */
@Slf4j
public class PodFilterServiceImpl implements PodFilterService {

    private PodFilterDao filterDao;
    private PodFilterMapper mapper;

    /**
     * Initialize new instance of {@link PodFilterServiceImpl}
     */
    PodFilterServiceImpl() {
        this.filterDao = new PodFilterDao();
        this.mapper = PodFilterMapper.INSTANCE;
    }

    /**
     * Gets the list of filters for pods
     */
    @Override
    public List<PodFilterModel> getFilters() {
        try {
            return this.mapper.entitiesToModels(this.filterDao.getFilters());
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Indicates when pod filter already exists in database
     *
     * @param filterValue - the value to search
     * @return true when pod filter with given value already present in database, false otherwise
     */
    @Override
    public boolean isFilterExists(String filterValue) {
        try {
            return this.filterDao.findByFilterValue(filterValue) != null;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * Creates or updates the filter for pod
     *
     * @param filter - the filter to create or update
     */
    @Override
    public void createOrUpdate(PodFilterModel filter) {
        try {
            this.filterDao.createOrUpdateFilter(this.mapper.modelToEntity(filter));
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Deletes the filter
     *
     * @param id - the identifier of filter to delete
     */
    @Override
    public void delete(int id) {
        try {
            this.filterDao.delete(id);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
