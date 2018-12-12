package com.tracker.domain.filter;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the filter entity for pod
 */
@Getter
@Setter
@DatabaseTable(tableName = "filters")
public class FilterEntity {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the filter value field name.
     */
    public static final String FILTER_VALUE_FIELD = "filter_value";

    /**
     * Provides the entity type field name.
     */
    public static final String ENTITY_TYPE_FIELD = "entity_type";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = FILTER_VALUE_FIELD)
    private String filterValue;

    @DatabaseField(columnName = ENTITY_TYPE_FIELD)
    private FilterType filterType;
}
