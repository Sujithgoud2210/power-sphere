package com.powersphere.notification.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class for the Notification module. Enables async processing,
 * scheduled tasks, JPA repositories, and entity scanning specific to the
 * notification package.
 */
@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "com.powersphere.notification")
@EntityScan(basePackages = "com.powersphere.notification.entity")
@EnableJpaRepositories(basePackages = "com.powersphere.notification.repository")
@PropertySource(value = "classpath:com/powersphere/notification/config/notification.properties", ignoreResourceNotFound = true)
public class NotificationConfig {

    /**
     * Configures a thread pool executor for async notification processing.
     * Handles notification dispatching, template rendering, and event handling
     * without blocking the main request thread.
     */
    @Bean(name = "notificationTaskExecutor")
    public Executor notificationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("notification-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}
