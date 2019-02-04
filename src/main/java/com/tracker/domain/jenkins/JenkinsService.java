package com.tracker.domain.jenkins;

import java.util.List;

/**
 * Provides the service for working with jenkins
 */
public interface JenkinsService {

    /**
     * Provides the model that described last status of test job
     *
     * @param environment - the environment where job was ran
     * @param testType - the type of tests
     * @return the {@link JenkinsJobModel} instance
     */
    JenkinsJobModel getTestJobStatus(JenkinsJobEnvironment environment, JenkinsJobTestSuite testType);

    /**
     * Gets the list of active builds from Ods-Pipeline and Pricing
     */
    List<JenkinsJobModel> getActiveBuilds();

}
