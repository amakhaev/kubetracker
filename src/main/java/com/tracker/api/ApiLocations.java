package com.tracker.api;

import lombok.experimental.UtilityClass;

/**
 * Provides the locations of API
 */
@UtilityClass
public class ApiLocations {

    /**
     * Provides the base part of any URL
     */
    public static final String ROOT = "/kubetracker";

    /**
     * Provides the settings URL
     */
    public static final String SETTINGS = ROOT + "/settings";

}
