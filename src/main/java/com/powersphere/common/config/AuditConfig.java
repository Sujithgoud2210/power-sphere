package com.powersphere.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * JPA Auditing configuration.
 * <p>
 * Provides automatic population of created_by, updated_by fields
 * in entity audit columns. Configured to use a placeholder auditor
 * until security/authentication is implemented.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * Provides the current auditor (user) for entity auditing.
     * Returns a placeholder value until authentication is implemented.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("SYSTEM");
    }
}
