package com.tracker.domain.settings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * Provides the implementation of settings service
 */
@Slf4j
@Service
public class SettingServiceImpl implements SettingService {

    private final SettingsDao settingsDao;
    private final SettingsModelMapper mapper;

    /**
     * Initialize new instance of {@link SettingServiceImpl}
     */
    @Autowired
    SettingServiceImpl(SettingsModelMapper mapper) {
        this.settingsDao = new SettingsDao();
        this.mapper = mapper;
    }

    /**
     * Gets the settings model
     */
    @Override
    public SettingsModel getSettings() {
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
     * @param settingsModel - the model to update
     * @return updated model
     */
    @Override
    public SettingsModel updateSettings(SettingsModel settingsModel) {
        try {
            return this.mapper.entityToModel(
                    this.settingsDao.update(this.mapper.modelToEntity(settingsModel))
            );
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
