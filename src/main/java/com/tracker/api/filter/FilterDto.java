package com.tracker.api.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides the read model for filter
 */
@Getter
@Setter
public class FilterDto {
    private int id;
    private String filterValue;
}
