package com.powersphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * PowerSphere – Enterprise Smart Energy Management Platform.
 * <p>
 * Main application entry point.
 * This modular monolith is designed with clean architecture principles
 * and can be split into microservices with minimal refactoring.
 */
@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties
public class PowerSphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowerSphereApplication.class, args);
    }
}
