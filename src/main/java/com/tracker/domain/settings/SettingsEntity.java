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
    public static final String KUBERNETES_NAME = "kubernetes_name";

    /**
     * Provides the short name field name.
     */
    public static final String JENKINS_NAME = "jenkins_name";

    /**
     * Provides the password field name.
     */
    public static final String PASSWORD_FIELD = "password";

    /**
     * Provides the jenkins api token field name.
     */
    public static final String JENKINS_API_TOKEN_FIELD = "jenkins_api_token";

    @DatabaseField(columnName = ID_FIELD, generatedId = true)
    private int id;

    @DatabaseField(columnName = KUBERNETES_NAME)
    private String kubernetesName;

    @DatabaseField(columnName = JENKINS_NAME)
    private String jenkinsName;

    @DatabaseField(columnName = PASSWORD_FIELD)
    private String password;

    @DatabaseField(columnName = JENKINS_API_TOKEN_FIELD)
    private String jenkinsApiToken;
}
