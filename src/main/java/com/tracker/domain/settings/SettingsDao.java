package com.tracker.domain.settings;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.tracker.configuration.DatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the DAO object to work with settings table
 */
@Slf4j
class SettingsDao {

    private Dao<SettingsEntity, Integer> settingsDao;

    /**
     * Initialize new instance of {@link SettingsDao}
     */
    SettingsDao() {
        try {
            this.settingsDao = DaoManager.createDao(DatabaseConfiguration.getConnection(), SettingsEntity.class);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Gets the application settings
     *
     * @return the {@link SettingsEntity} instance
     */
    public SettingsEntity getSettings() throws SQLException {
        return this.settingsDao.queryBuilder().queryForFirst();
    }

    /**
     * Updates the settings entity
     *
     * @param entity - the settings to update
     * @return updated {@link SettingsEntity} instance
     */
    public SettingsEntity update(SettingsEntity entity) throws SQLException {
        this.settingsDao.update(entity);
        return this.getSettings();
    }
}
