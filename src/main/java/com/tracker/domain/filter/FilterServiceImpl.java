package com.tracker.domain.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Provides the implementation of {@link FilterService}
 */
@Slf4j
@Service
public class FilterServiceImpl implements FilterService {

    private final FilterDao filterDao;
    private FilterMapper mapper;

    /**
     * Initialize new instance of {@link FilterServiceImpl}
     */
    @Autowired
    FilterServiceImpl(FilterDao filterDao) {
        this.filterDao = filterDao;
        this.mapper = FilterMapper.INSTANCE;
    }

    /**
     * Gets the list of filters for podList
     */
    @Override
    public List<FilterModel> getFilters(FilterType filterType) {
        try {
            return this.mapper.entitiesToModels(this.filterDao.getFilters(filterType));
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
    public boolean isFilterExists(String filterValue, FilterType filterType) {
        try {
            return this.filterDao.findByFilterValue(filterValue, filterType) != null;
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
    public void createOrUpdate(FilterModel filter) {
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
