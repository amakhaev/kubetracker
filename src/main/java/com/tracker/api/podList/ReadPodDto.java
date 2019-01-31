package com.tracker.api.podList;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the dto of POD
 */
@Getter
@Setter
public class ReadPodDto {

    private String name;
    private String namespace;
    private String status;
    private Integer restartCount;
    private String startedAt;
    private boolean isReady;

}
