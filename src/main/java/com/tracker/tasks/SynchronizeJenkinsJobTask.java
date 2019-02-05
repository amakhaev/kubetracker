package com.tracker.tasks;

import com.tracker.domain.jenkins.JenkinsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Provides the scheduled task to synchronize jenkins jobs
 */
@Component
public class SynchronizeJenkinsJobTask {

    @Autowired
    private JenkinsService jenkinsService;

    @Scheduled(fixedDelay = 60000)
    public void scheduleFixedDelayTask() {
        this.jenkinsService.synchronizeJobs();
    }

}
