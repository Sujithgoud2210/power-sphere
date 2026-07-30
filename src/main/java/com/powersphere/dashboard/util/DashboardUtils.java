package com.powersphere.dashboard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class providing helper methods for dashboard data processing.
 */
public final class DashboardUtils {

    private static final Logger log = LoggerFactory.getLogger(DashboardUtils.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DashboardUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Safely parses a date string into LocalDate.
     *
     * @param dateStr the date string in yyyy-MM-dd format
     * @return parsed LocalDate, or null if parsing fails
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse date: {}", dateStr);
            return null;
        }
    }

    /**
     * Safely parses a date-time string into LocalDateTime.
     *
     * @param dateTimeStr the date-time string in yyyy-MM-dd HH:mm:ss format
     * @return parsed LocalDateTime, or null if parsing fails
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse date-time: {}", dateTimeStr);
            return null;
        }
    }

    /**
     * Formats a LocalDate to string.
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Calculates percentage change between two values.
     *
     * @param current the current value
     * @param previous the previous value
     * @return percentage change rounded to 2 decimal places
     */
    public static double calculatePercentageChange(double current, double previous) {
        if (previous == 0) {
            return current > 0 ? 100.0 : 0.0;
        }
        return BigDecimal.valueOf((current - previous) / previous * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Converts kilowatt-hours to megawatt-hours.
     */
    public static double kwhToMwh(double kwh) {
        return BigDecimal.valueOf(kwh / 1000.0)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Validates a period string.
     *
     * @param period the period to validate
     * @return true if period is DAILY, MONTHLY, or YEARLY (case-insensitive)
     */
    public static boolean isValidPeriod(String period) {
        if (period == null) {
            return false;
        }
        String upper = period.toUpperCase();
        return "DAILY".equals(upper) || "MONTHLY".equals(upper) || "YEARLY".equals(upper);
    }

    /**
     * Returns the start of the current day.
     */
    public static LocalDateTime startOfToday() {
        return LocalDate.now().atStartOfDay();
    }

    /**
     * Returns the end of the current day.
     */
    public static LocalDateTime endOfToday() {
        return LocalDate.now().atTime(23, 59, 59, 999999999);
    }
}
