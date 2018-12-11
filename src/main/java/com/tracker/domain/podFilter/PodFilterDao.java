package com.tracker.domain.podFilter;

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
class PodFilterDao {

    private Dao<PodFilterEntity, Integer> podFilterDao;

    /**
     * Initialize new instance of {@link PodFilterDao}
     */
    PodFilterDao() {
        try {
            this.podFilterDao = DaoManager.createDao(DatabaseConfiguration.getConnection(), PodFilterEntity.class);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets all filters for pod
     *
     * @return the {@link List<PodFilterEntity>} instance.
     */
    List<PodFilterEntity> getFilters() throws SQLException {
        return this.podFilterDao.queryForAll();
    }

    /**
     * Creates the pod filter
     *
     * @param podFilter - the filter to create
     */
    void createOrUpdateFilter(PodFilterEntity podFilter) throws SQLException {
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
     * @return the {@link PodFilterEntity} instance
     */
    PodFilterEntity findByFilterValue(String value) throws SQLException {
        return this.podFilterDao.queryBuilder().where().like(PodFilterEntity.FILTER_VALUE_FIELD, value).queryForFirst();
    }
}
