package com.tracker.domain.settings;

/**
 * Provides the setting service
 */
public interface SettingService {

    /**
     * Gets the settings model
     */
    SettingsModel getSettings();

    /**
     * Updates the settings model
     *
     * @param settingsModel - the model to update
     * @return updated model
     */
    SettingsModel updateSettings(SettingsModel settingsModel);

}
