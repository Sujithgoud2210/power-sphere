package com.powersphere.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Cache configuration for the PowerSphere platform.
 * <p>
 * Uses Redis as the primary cache store in production.
 * Falls back to simple in-memory caching for development.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Default cache names used across the platform.
     */
    public static final String CACHE_USERS = "users";
    public static final String CACHE_ORGANIZATIONS = "organizations";
    public static final String CACHE_METERS = "meters";
    public static final String CACHE_ENERGY_DATA = "energyData";
    public static final String CACHE_BILLING = "billing";

    /**
     * Fallback cache manager for development environments.
     *
     * @return ConcurrentMapCacheManager
     */
    @Bean
    @Profile({"dev", "test"})
    public CacheManager devCacheManager() {
        return new ConcurrentMapCacheManager(
                CACHE_USERS,
                CACHE_ORGANIZATIONS,
                CACHE_METERS,
                CACHE_ENERGY_DATA,
                CACHE_BILLING
        );
    }
}
