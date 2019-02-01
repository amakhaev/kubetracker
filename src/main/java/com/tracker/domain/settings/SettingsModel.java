package com.tracker.domain.settings;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model that represented the settings
 */
@Getter
@Setter
public class SettingsModel {

    private int id;
    private String fullName;
    private String shortName;
    private String password;
    private String jenkinsApiToken;

}
