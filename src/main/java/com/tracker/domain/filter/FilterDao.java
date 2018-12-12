package com.tracker.domain.filter;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tracker.configuration.DatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

/**
 * Provides the DAO for pod filters model
 */
@Slf4j
class FilterDao {

    private Dao<FilterEntity, Integer> podFilterDao;

    /**
     * Initialize new instance of {@link FilterDao}
     */
    FilterDao() {
        try {
            this.podFilterDao = DaoManager.createDao(DatabaseConfiguration.getConnection(), FilterEntity.class);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets all filters for pod
     *
     * @return the {@link List< FilterEntity >} instance.
     */
    List<FilterEntity> getFilters(FilterType filterType) throws SQLException {
        return this.podFilterDao.queryBuilder().where().eq(FilterEntity.ENTITY_TYPE_FIELD, filterType).query();
    }

    /**
     * Creates the pod filter
     *
     * @param podFilter - the filter to create
     */
    void createOrUpdateFilter(FilterEntity podFilter) throws SQLException {
        this.podFilterDao.createOrUpdate(podFilter);
    }

    /**
     * Deletes the pod filter
     *
     * @param id - the identifier of filter to delete
     */
    void delete(int id) throws SQLException {
        this.podFilterDao.deleteById(id);
    }

    /**
     * Gets the pod filter by given filter value
     *
     * @param value - the filter value to search
     * @return the {@link FilterEntity} instance
     */
    FilterEntity findByFilterValue(String value, FilterType filterType) throws SQLException {
        return this.podFilterDao.queryBuilder()
                .where()
                .like(FilterEntity.FILTER_VALUE_FIELD, value)
                .and()
                .eq(FilterEntity.ENTITY_TYPE_FIELD, filterType)
                .queryForFirst();
    }
}
