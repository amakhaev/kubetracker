package com.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Provides the application file related to
 */
@SpringBootApplication
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class OpenPricingTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenPricingTrackerApplication.class, args);

        /*SwingUtilities.invokeLater(() -> {
            KubeTrackerWidget kubeTrackerWidget = new KubeTrackerWidget();
            kubeTrackerWidget.show();
            kubeTrackerWidget.initialize();
        });*/
    }

}
