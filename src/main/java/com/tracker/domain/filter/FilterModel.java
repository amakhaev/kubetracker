package com.tracker.domain.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the filter model for pod
 */
@Getter
@Setter
public class FilterModel {

    private int id;
    private String filterValue;
    private FilterType filterType;

}
