package com.tracker.api.settings;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the write dto for settings
 */
@Getter
@Setter
public class WriteSettingsDto {

    private int id;
    private String kubernetesName;
    private String jenkinsName;
    private String password;
    private String jenkinsApiToken;

}
