package com.tracker.api.jenkins;

import com.tracker.domain.jenkins.JenkinsFolder;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the dto of jenkins job
 */
@Getter
@Setter
public class JenkinsJobDto {

    private String name;
    private String displayName;
    private int buildNumber;
    private long duration;
    private long estimateDuration;
    private String result;
    private long startedAt;
    private String url;
    private JenkinsFolder folder;

}
