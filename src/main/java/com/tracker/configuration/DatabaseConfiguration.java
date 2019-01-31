package com.tracker.configuration;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Provides the database configuration.
 */
@Slf4j
@Configuration
public class DatabaseConfiguration {

    @Value("${db.name:kubetracker.db3}")
    private String dbName;

    @Bean
    public ConnectionSource sqliteConnection() {
        try {
            String url = "jdbc:sqlite:" + this.dbName;
            ConnectionSource sqliteConnection = new JdbcConnectionSource(url);
            log.info("Connection to SQLite has been established.");
            return sqliteConnection;
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
