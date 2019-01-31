package com.tracker.domain.pods;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the model of POD
 */
@Getter
@Setter
public class PodModel {

    private String name;
    private String namespace;
    private String status;
    private Integer restartCount;
    private String startedAt;
    private boolean isReady;

}
