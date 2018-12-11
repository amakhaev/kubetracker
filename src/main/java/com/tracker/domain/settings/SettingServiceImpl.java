package com.tracker.domain.settings;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the implementation of settings service
 */
@Slf4j
public class SettingServiceImpl implements SettingService {

    private SettingsDao settingsDao;
    private SettingsMapper mapper;

    /**
     * Initialize new instance of {@link SettingServiceImpl}
     */
    SettingServiceImpl() {
        this.settingsDao = new SettingsDao();
        this.mapper = SettingsMapper.INSTANCE;
    }

    /**
     * Gets the settings model
     */
    @Override
    public SettingModel getSettings() {
        try {
            return this.mapper.entityToModel(this.settingsDao.getSettings());
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Updates the settings model
     *
     * @param settingModel - the model to update
     * @return updated model
     */
    @Override
    public SettingModel updateSettings(SettingModel settingModel) {
        try {
            return this.mapper.entityToModel(
                    this.settingsDao.update(this.mapper.modelToEntity(settingModel))
            );
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
