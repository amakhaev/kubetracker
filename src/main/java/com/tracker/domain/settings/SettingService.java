package com.tracker.domain.settings;

/**
 * Provides the setting service
 */
public interface SettingService {

    SettingService INSTANCE = new SettingServiceImpl();

    /**
     * Gets the settings model
     */
    SettingModel getSettings();

    /**
     * Updates the settings model
     *
     * @param settingModel - the model to update
     * @return updated model
     */
    SettingModel updateSettings(SettingModel settingModel);

}
