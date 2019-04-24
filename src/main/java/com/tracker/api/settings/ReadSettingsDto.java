package com.tracker.api.settings;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the dto for settings
 */
@Getter
@Setter
public class ReadSettingsDto {

    private int id;
    private String kubernetesName;
    private String jenkinsName;
    private String jenkinsApiToken;

}
