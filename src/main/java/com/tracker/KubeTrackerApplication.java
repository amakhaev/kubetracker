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
public class KubeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KubeTrackerApplication.class, args);

        /*SwingUtilities.invokeLater(() -> {
            KubeTrackerWidget kubeTrackerWidget = new KubeTrackerWidget();
            kubeTrackerWidget.show();
            kubeTrackerWidget.initialize();
        });*/
    }

}
