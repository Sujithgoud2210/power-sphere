package com.powersphere.common.constant;

/**
 * Centralized application constants for PowerSphere platform.
 * <p>
 * Contains commonly used values across the application.
 * Constants should be organized by functional area.
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ===== Application Metadata =====
    public static final String APP_NAME = "PowerSphere";
    public static final String APP_DESCRIPTION = "Enterprise Smart Energy Management Platform";
    public static final String APP_VERSION = "1.0.0-SNAPSHOT";
    public static final String BASE_PACKAGE = "com.powersphere";

    // ===== API Constants =====
    public static final String API_BASE_PATH = "/api/v1";
    public static final String API_DOC_PATH = "/api-docs";

    // ===== Date/Time Formats =====
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ISO_TIME_FORMAT = "HH:mm:ss";

    // ===== Pagination =====
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // ===== Cache Constants =====
    public static final String CACHE_MANAGER_BEAN = "cacheManager";
    public static final String DEFAULT_CACHE_TTL = "3600";

    // ===== Async Constants =====
    public static final String ASYNC_TASK_EXECUTOR_BEAN = "taskExecutor";
    public static final int ASYNC_CORE_POOL_SIZE = 5;
    public static final int ASYNC_MAX_POOL_SIZE = 10;
    public static final int ASYNC_QUEUE_CAPACITY = 100;

    // ===== Profile Constants =====
    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_TEST = "test";
    public static final String PROFILE_PROD = "prod";
}
