package com.powersphere.report.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class providing helper methods for report generation.
 */
public final class ReportUtils {

    private static final Logger log = LoggerFactory.getLogger(ReportUtils.class);
    private static final DateTimeFormatter REPORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private ReportUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Generates a report identifier based on type and date.
     *
     * @param reportType the type of report (DAILY, WEEKLY, MONTHLY, YEARLY)
     * @param date       the reference date
     * @return formatted report ID string
     */
    public static String generateReportId(String reportType, LocalDate date) {
        String datePart = date.format(REPORT_DATE_FORMATTER);
        return reportType + "_" + datePart;
    }

    /**
     * Calculates the start date of the week containing the given date (Monday).
     */
    public static LocalDate getWeekStart(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * Calculates the end date of the week containing the given date (Sunday).
     */
    public static LocalDate getWeekEnd(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    /**
     * Returns the last day of the month for the given date.
     */
    public static LocalDate getMonthEnd(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    /**
     * Returns the first day of the month for the given date.
     */
    public static LocalDate getMonthStart(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    /**
     * Validates a report type string.
     *
     * @param reportType the report type to validate
     * @return true if valid (DAILY, WEEKLY, MONTHLY, YEARLY, CONSUMPTION, BILLING, ORGANIZATION)
     */
    public static boolean isValidReportType(String reportType) {
        if (reportType == null) {
            return false;
        }
        String upper = reportType.toUpperCase();
        return "DAILY".equals(upper) || "WEEKLY".equals(upper) || "MONTHLY".equals(upper)
                || "YEARLY".equals(upper) || "CONSUMPTION".equals(upper) || "BILLING".equals(upper)
                || "ORGANIZATION".equals(upper);
    }

    /**
     * Calculates the number of days in a report period.
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return start.until(end).getDays() + 1;
    }
}
