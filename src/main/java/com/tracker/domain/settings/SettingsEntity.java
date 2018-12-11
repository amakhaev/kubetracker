package com.tracker.domain.settings;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

/**
 * Provides the settings entity
 */
@Getter
@Setter
@DatabaseTable(tableName = "settings")
public class SettingsEntity {

    /**
     * Provides the ID field name.
     */
    public static final String ID_FIELD = "id";

    /**
     * Provides the full name field name.
     */
    public static final String FULL_NAME_FIELD = "full_name";

    /**
     * Provides the short name field name.
     */
    public static final String SHORT_NAME_FIELD = "short_name";

    /**
     * Provides the password field name.
     */
    public static final String PASSWORD_FIELD = "password";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = FULL_NAME_FIELD)
    private String fullName;

    @DatabaseField(columnName = SHORT_NAME_FIELD)
    private String shortName;

    @DatabaseField(columnName = PASSWORD_FIELD)
    private String password;
}
