package com.powersphere.common;

/**
 * Common Module – PowerSphere Enterprise Platform.
 * <p>
 * This module contains shared utilities, base classes, DTOs, constants,
 * and configuration that are reused across all other modules.
 * <p>
 * Package organization:
 * <ul>
 *   <li>{@code config} – Shared configuration classes (e.g., AuditConfig)</li>
 *   <li>{@code constant} – Application-wide constants and validation messages</li>
 *   <li>{@code dto} – Generic Data Transfer Objects (ApiResponse, ErrorResponse)</li>
 *   <li>{@code entity} – Base JPA entity with auditing fields</li>
 *   <li>{@code exception} – Global exception handling infrastructure</li>
 * </ul>
 */
public final class PackageInfo {

    private PackageInfo() {
        throw new UnsupportedOperationException("Marker class cannot be instantiated");
    }

    /**
     * Returns the module name.
     *
     * @return module name
     */
    public static String getModuleName() {
        return "common";
    }
}
