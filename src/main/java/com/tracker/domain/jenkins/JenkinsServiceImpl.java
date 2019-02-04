package com.tracker.domain.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.*;
import com.tracker.domain.common.exception.BadRequestException;
import com.tracker.domain.common.exception.EntityNotFoundException;
import com.tracker.domain.common.exception.UnauthorizedException;
import com.tracker.domain.settings.SettingService;
import com.tracker.domain.settings.SettingsModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the implementation of {@link JenkinsService}
 */
@Service
@Slf4j
public class JenkinsServiceImpl implements JenkinsService {

    private static final String JOB_NAME = "Pricing";

    private final String devSmokeTestsUrl;
    private final String devEndToEndTestsUrl;
    private final String qaSmokeTestsUrl;
    private final String qaEndToEndTestsUrl;
    private final String planFolderUrl;
    private final String odsPipelineFolderUrl;

    private final SettingService settingService;

    /**
     * Initialize new instance of {@link JenkinsServiceImpl}
     */
    @Autowired
    public JenkinsServiceImpl(@Value("${jenkins.dev_smoke_test_url}") String devSmokeTestsUrl,
                              @Value("${jenkins.dev_e2e_test_url}") String devEndToEndTestsUrl,
                              @Value("${jenkins.qa_smoke_test_url}") String qaSmokeTestsUrl,
                              @Value("${jenkins.qa_e2e_test_url}") String qaEndToEndTestsUrl,
                              @Value("${jenkins.plan_folder_url}") String planFolderUrl,
                              @Value("${jenkins.ods_pipeline_folder_url}") String odsPipelineFolderUrl,
                              SettingService settingService) {
        this.settingService = settingService;
        this.devSmokeTestsUrl = devSmokeTestsUrl;
        this.devEndToEndTestsUrl = devEndToEndTestsUrl;
        this.qaSmokeTestsUrl = qaSmokeTestsUrl;
        this.qaEndToEndTestsUrl = qaEndToEndTestsUrl;
        this.planFolderUrl = planFolderUrl;
        this.odsPipelineFolderUrl = odsPipelineFolderUrl;
    }

    /**
     * Provides the model that described last status of test job
     *
     * @param environment - the environment where job was ran
     * @param testSuite    - the suite of tests
     * @return the {@link JenkinsJobModel} instance
     */
    @Override
    public JenkinsJobModel getTestJobStatus(JenkinsJobEnvironment environment, JenkinsJobTestSuite testSuite) {
        log.info("Try to retrieve data related to UI tests. Environment: {}, Suite: {}", environment, testSuite);
        if (environment == JenkinsJobEnvironment.DEV) {
            if (testSuite == JenkinsJobTestSuite.SMOKE) {
                return this.getUiTestJenkinsJob(this.devSmokeTestsUrl, JenkinsFolder.DEV_SMOKE);
            } else if (testSuite == JenkinsJobTestSuite.END_TO_END) {
                return this.getUiTestJenkinsJob(this.devEndToEndTestsUrl, JenkinsFolder.DEV_END_TO_END);
            }
        } else if (environment == JenkinsJobEnvironment.QA) {
            if (testSuite == JenkinsJobTestSuite.SMOKE) {
                return this.getUiTestJenkinsJob(this.qaSmokeTestsUrl, JenkinsFolder.QA_SMOKE);
            } else if (testSuite == JenkinsJobTestSuite.END_TO_END) {
                return this.getUiTestJenkinsJob(this.qaEndToEndTestsUrl, JenkinsFolder.QA_END_TO_END);
            }
        } else {
            throw new BadRequestException("Incorrect environment or suite");
        }

        return null;
    }

    /**
     * Gets the list of active builds from Ods-Pipeline and Pricing
     */
    @Override
    public List<JenkinsJobModel> getActiveBuilds() {
        log.info("Try to get active builds");
        List<JenkinsJobModel> result = new ArrayList<>();

        SettingsModel settingsModel = this.settingService.getSettings();
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(
                    new URI(this.planFolderUrl), settingsModel.getFullName(), settingsModel.getJenkinsApiToken()
            );

            log.info("Retrieve active builds for {}", JenkinsFolder.PLAN);
            result.addAll(this.findActiveBuilds(jenkinsServer.getJobs(), JenkinsFolder.PLAN));

            jenkinsServer = new JenkinsServer(
                    new URI(this.odsPipelineFolderUrl), settingsModel.getFullName(), settingsModel.getJenkinsApiToken()
            );

            log.info("Retrieve active builds for {}", JenkinsFolder.ODS_PIPELINE);
            result.addAll(this.findActiveBuilds(jenkinsServer.getJobs(), JenkinsFolder.ODS_PIPELINE));
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 401) {
                throw new UnauthorizedException(e.getMessage());
            }
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            return null;
        }

        return result;
    }

    private List<JenkinsJobModel> findActiveBuilds(Map<String, Job> jobs, JenkinsFolder folder) {
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        List<JenkinsJobModel> result = new ArrayList<>();
        jobs.entrySet()
                .parallelStream()
                .forEach(entry -> {
                    try {
                        JobWithDetails jobWithDetails = entry.getValue().details();
                        if (jobWithDetails == null) {
                            return;
                        }

                        Build lastBuild = jobWithDetails.getLastBuild();
                        if (lastBuild == null) {
                            return;
                        }

                        BuildWithDetails buildWithDetails = lastBuild.details();
                        if (buildWithDetails == null) {
                            return;
                        }

                        BuildResult buildResult = buildWithDetails.getResult();
                        if (buildResult == null) {
                            log.info("Found active build: {}", entry.getKey());
                            result.add(this.createJobModel(jobWithDetails, folder));
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });

        return result;
    }

    private JenkinsJobModel getUiTestJenkinsJob(String url, JenkinsFolder folder) {
        SettingsModel settingsModel = this.settingService.getSettings();
        JobWithDetails jobWithDetails;
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(
                    new URI(url), settingsModel.getFullName(), settingsModel.getJenkinsApiToken()
            );
            jobWithDetails = jenkinsServer.getJob(JOB_NAME);
        } catch (HttpResponseException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            return null;
        }

        return this.createJobModel(jobWithDetails, folder);
    }

    private JenkinsJobModel createJobModel(JobWithDetails jobWithDetails, JenkinsFolder folder) {
        if (jobWithDetails == null) {
            throw new EntityNotFoundException("Failed to find the job");
        }

        Build lastBuild = jobWithDetails.getLastBuild();

        JenkinsJobModel jenkinsJobModel = new JenkinsJobModel();
        jenkinsJobModel.setName(jobWithDetails.getName());
        jenkinsJobModel.setFolder(folder);

        try {
            if (lastBuild != null) {
                BuildWithDetails buildWithDetails = lastBuild.details();

                jenkinsJobModel.setUrl(lastBuild.getUrl());
                jenkinsJobModel.setDisplayName(buildWithDetails.getFullDisplayName());
                jenkinsJobModel.setBuildNumber(lastBuild.getNumber());
                jenkinsJobModel.setDuration(buildWithDetails.getDuration());
                jenkinsJobModel.setEstimateDuration(buildWithDetails.getEstimatedDuration());
                jenkinsJobModel.setResult(buildWithDetails.getResult() == null ? null : buildWithDetails.getResult().name());
                jenkinsJobModel.setStartedAt(buildWithDetails.getTimestamp());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return jenkinsJobModel;
    }
}
