package com.tracker.domain.podFilter;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the filter entity for pod
 */
@Getter
@Setter
@DatabaseTable(tableName = "pod_filters")
public class PodFilterEntity {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the filter value field name.
     */
    public static final String FILTER_VALUE_FIELD = "filter_value";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = FILTER_VALUE_FIELD)
    private String filterValue;
}
