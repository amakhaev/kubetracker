package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Provides the application file related to
 */
@SpringBootApplication
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@EnableScheduling
public class OpenPricingTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenPricingTrackerApplication.class, args);
    }

}
