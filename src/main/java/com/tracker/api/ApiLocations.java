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
     * Provides the jenkins URL
     */
    public static final String JENKINS = ROOT + "/jenkins";

    /**
     * Provides the settings URL
     */
    public static final String SETTINGS = ROOT + "/settings";

    /**
     * Provides the pod list URL
     */
    public static final String POD_LIST = ROOT + "/pods";

    /**
     * Provides the filter URL
     */
    public static final String FILTERS = ROOT + "/filters";
}
