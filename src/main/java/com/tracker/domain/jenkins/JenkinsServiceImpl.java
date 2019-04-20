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
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Provides the implementation of {@link JenkinsService}
 */
@Service
@Slf4j
public class JenkinsServiceImpl implements JenkinsService {

    private static final String UI_TEST_JOB_NAME = "Pricing";

    private List<JenkinsJobModel> cachedJenkinsJobs;

    private final String devSmokeTestsUrl;
    private final String devEndToEndTestsUrl;
    private final String qaSmokeTestsUrl;
    private final String qaEndToEndTestsUrl;
    private final String planFolderUrl;
    private final String odsPipelineFolderUrl;

    private final SettingService settingService;
    private final ExecutorService executorService;

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
                              SettingService settingService,
                              ExecutorService executorService) {
        this.settingService = settingService;
        this.devSmokeTestsUrl = devSmokeTestsUrl;
        this.devEndToEndTestsUrl = devEndToEndTestsUrl;
        this.qaSmokeTestsUrl = qaSmokeTestsUrl;
        this.qaEndToEndTestsUrl = qaEndToEndTestsUrl;
        this.planFolderUrl = planFolderUrl;
        this.odsPipelineFolderUrl = odsPipelineFolderUrl;
        this.executorService = executorService;
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
        log.info("Try to get active builds from cache");
        if (this.cachedJenkinsJobs == null) {
            log.info("Cache is empty");
            return Collections.emptyList();
        }

        return this.cachedJenkinsJobs.stream()
                .filter(jenkinsJobModel -> jenkinsJobModel.getResult() == null)
                .collect(Collectors.toList());
    }

    /**
     * Gets the last @code count completed builds
     *
     * @param count - the count of builds
     * @return List of {@link JenkinsJobModel}
     */
    @Override
    public List<JenkinsJobModel> getLastCompletedBuilds(int count) {
        log.info("Try to get last completed builds from cache");
        if (this.cachedJenkinsJobs == null) {
            log.info("Cache is empty");
            return Collections.emptyList();
        }

        return this.cachedJenkinsJobs.stream()
                .filter(jenkinsJobModel -> jenkinsJobModel.getResult() != null)
                .sorted(Comparator.comparing(JenkinsJobModel::getFinishedAt).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Synchronizes all jobs from jenkins
     */
    @Override
    public void synchronizeJobs() {
        log.info("Start synchronization");
        List<Callable<JenkinsJobModel>> tasks = null;
        try {
            tasks = this.getTasksForPlanAndPipelineJobs();
        } catch (URISyntaxException | IOException e) {
            log.error(e.getMessage());
            this.cachedJenkinsJobs = Collections.emptyList();
            return;
        }

        try {
            List<Future<JenkinsJobModel>> futures = this.executorService.invokeAll(tasks);
            log.info("{} jobs retrieved", futures.size());
            List<JenkinsJobModel> jobsToReplace = futures.stream()
                    .map((future) -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            log.error(e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            this.cachedJenkinsJobs = Collections.synchronizedList(jobsToReplace);
            log.info("Synchronization completed. Saved item count: {}", futures.size());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            this.cachedJenkinsJobs = Collections.emptyList();
        }
    }

    private JenkinsJobModel getUiTestJenkinsJob(String url, JenkinsFolder folder) {
        SettingsModel settingsModel = this.settingService.getSettings();
        JobWithDetails jobWithDetails;
        try {
            JenkinsServer jenkinsServer = new JenkinsServer(
                    new URI(url), settingsModel.getJenkinsName(), settingsModel.getJenkinsApiToken()
            );
            jobWithDetails = jenkinsServer.getJob(UI_TEST_JOB_NAME);
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
            throw new EntityNotFoundException("Failed to create the job");
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

    private List<Callable<JenkinsJobModel>> getTasksForPlanAndPipelineJobs() throws URISyntaxException, IOException {
        SettingsModel settingsModel = this.settingService.getSettings();
        List<Callable<JenkinsJobModel>> tasks = new ArrayList<>();
        JenkinsServer jenkinsServer = new JenkinsServer(
                new URI(this.planFolderUrl), settingsModel.getJenkinsName(), settingsModel.getJenkinsApiToken()
        );

        log.info("Create callable tasks for {} folder", JenkinsFolder.PLAN);
        tasks.addAll(jenkinsServer.getJobs().entrySet()
                .stream()
                .map(entry -> (Callable<JenkinsJobModel>) () -> this.createJobModel(entry.getValue().details(), JenkinsFolder.PLAN))
                .collect(Collectors.toList())
        );

        jenkinsServer = new JenkinsServer(
                new URI(this.odsPipelineFolderUrl), settingsModel.getJenkinsName(), settingsModel.getJenkinsApiToken()
        );

        log.info("Create callable tasks for {} folder", JenkinsFolder.ODS_PIPELINE);
        tasks.addAll(jenkinsServer.getJobs().entrySet()
                .stream()
                .map(entry -> (Callable<JenkinsJobModel>) () -> this.createJobModel(entry.getValue().details(), JenkinsFolder.ODS_PIPELINE))
                .collect(Collectors.toList())
        );

        log.info("All tasks created successfully, size: {}", tasks.size());
        return tasks;
    }
}
