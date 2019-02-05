package com.tracker.domain.jenkins;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model of jenkins job
 */
@Getter
@Setter
public class JenkinsJobModel {

    private String name;
    private String displayName;
    private int buildNumber;
    private long duration;
    private long estimateDuration;
    private String result;
    private long startedAt;
    private String url;
    private JenkinsFolder folder;

    public long getFinishedAt() {
        return this.startedAt + this.duration;
    }
}
