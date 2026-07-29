package com.powersphere.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA (Java Persistence API) configuration.
 * <p>
 * Configures entity scanning, repository scanning, transaction management,
 * and JPA-related settings for the PowerSphere platform.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.powersphere")
@EntityScan(basePackages = "com.powersphere")
public class JpaConfig {

    // JPA configuration is handled via application.yml properties.
    // Additional JPA customization (e.g., NamingStrategy, Dialect) 
    // can be added here as needed during module implementation.
}
