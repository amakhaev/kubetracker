package com.tracker.domain.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.JobWithDetails;
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
    private final SettingService settingService;

    /**
     * Initialize new instance of {@link JenkinsServiceImpl}
     */
    @Autowired
    public JenkinsServiceImpl(@Value("${jenkins.dev_smoke_test_url}") String devSmokeTestsUrl,
                              @Value("${jenkins.dev_e2e_test_url}") String devEndToEndTestsUrl,
                              @Value("${jenkins.qa_smoke_test_url}") String qaSmokeTestsUrl,
                              @Value("${jenkins.qa_e2e_test_url}") String qaEndToEndTestsUrl,
                              SettingService settingService) {
        this.settingService = settingService;
        this.devSmokeTestsUrl = devSmokeTestsUrl;
        this.devEndToEndTestsUrl = devEndToEndTestsUrl;
        this.qaSmokeTestsUrl = qaSmokeTestsUrl;
        this.qaEndToEndTestsUrl = qaEndToEndTestsUrl;
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
                return this.getJenkinsJob(this.devSmokeTestsUrl);
            } else if (testSuite == JenkinsJobTestSuite.END_TO_END) {
                return this.getJenkinsJob(this.devEndToEndTestsUrl);
            }
        } else if (environment == JenkinsJobEnvironment.QA) {
            if (testSuite == JenkinsJobTestSuite.SMOKE) {
                return this.getJenkinsJob(this.qaSmokeTestsUrl);
            } else if (testSuite == JenkinsJobTestSuite.END_TO_END) {
                return this.getJenkinsJob(this.qaEndToEndTestsUrl);
            }
        } else {
            throw new BadRequestException("Incorrect environment or suite");
        }

        return null;
    }

    private JenkinsJobModel getJenkinsJob(String url) {
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

        if (jobWithDetails == null) {
            throw new EntityNotFoundException("Failed to find the job");
        }

        Build lastBuild = jobWithDetails.getLastBuild();

        JenkinsJobModel jenkinsJobModel = new JenkinsJobModel();
        jenkinsJobModel.setName(jobWithDetails.getName());

        try {
            if (lastBuild != null) {
                jenkinsJobModel.setUrl(lastBuild.getUrl());
                jenkinsJobModel.setDisplayName(lastBuild.details().getFullDisplayName());
                jenkinsJobModel.setBuildNumber(lastBuild.getNumber());
                jenkinsJobModel.setDuration(lastBuild.details().getDuration());
                jenkinsJobModel.setEstimateDuration(lastBuild.details().getEstimatedDuration());
                jenkinsJobModel.setResult(lastBuild.details().getResult() == null ? null : lastBuild.details().getResult().name());
                jenkinsJobModel.setStartedAt(lastBuild.details().getTimestamp());
                jenkinsJobModel.setResult(lastBuild.details().getResult() == null ? null : lastBuild.details().getResult().name());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return jenkinsJobModel;
    }
}
