package com.tracker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides the worker for async tasks
 */
@Configuration
public class ExecutorServiceConfiguration {

    private static final int THREAD_POOL_COUNT = 15;

    @Bean
    public ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(THREAD_POOL_COUNT);
    }

}
