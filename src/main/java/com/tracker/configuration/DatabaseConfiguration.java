package com.tracker.configuration;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.tracker.utils.ResourceHelper;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Provides the database configuration.
 */
@Slf4j
public class DatabaseConfiguration {

    private static ConnectionSource sqliteConnection;

    private DatabaseConfiguration() {}

    /**
     * Gets the database connection
     */
    public static ConnectionSource getConnection() {
        if (sqliteConnection == null) {
            connect();
        }

        return sqliteConnection;
    }

    private static void connect() {
        try {
            String url = "jdbc:sqlite:" + ResourceHelper.getProperty("db");
            sqliteConnection = new JdbcConnectionSource(url);
            log.info("Connection to SQLite has been established.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
