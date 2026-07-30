package com.powersphere.energy.util;

import lombok.experimental.UtilityClass;

/**
 * Utility methods for safe enum operations.
 */
@UtilityClass
public class EnumUtils {

    /**
     * Safely parses an enum value from a string, returning null if the string is blank or invalid.
     *
     * @param enumClass the enum class
     * @param value     the string value to parse
     * @param <T>       the enum type
     * @return the parsed enum value, or null if parsing fails
     */
    public static <T extends Enum<T>> T safeParseEnum(Class<T> enumClass, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
