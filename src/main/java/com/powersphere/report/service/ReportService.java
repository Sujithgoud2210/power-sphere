package com.powersphere.report.service;

import com.powersphere.report.dto.ReportResponse;

import java.time.LocalDate;

public interface ReportService {

    /**
     * Generates a daily report with summary data for the given date.
     *
     * @param date the date for the daily report (defaults to today if null)
     * @return populated ReportResponse
     */
    ReportResponse generateDailyReport(LocalDate date);

    /**
     * Generates a weekly report for the given week.
     *
     * @param startDate the start date of the week (defaults to current week if null)
     * @return populated ReportResponse
     */
    ReportResponse generateWeeklyReport(LocalDate startDate);

    /**
     * Generates a monthly report for the given month.
     *
     * @param year  the year
     * @param month the month (1-12)
     * @return populated ReportResponse
     */
    ReportResponse generateMonthlyReport(int year, int month);

    /**
     * Generates a yearly report for the given year.
     *
     * @param year the year (defaults to current year if <= 0)
     * @return populated ReportResponse
     */
    ReportResponse generateYearlyReport(int year);

    /**
     * Generates a consumption-focused report for a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return populated ReportResponse
     */
    ReportResponse generateConsumptionReport(LocalDate startDate, LocalDate endDate);

    /**
     * Generates a billing-focused report for a date range.
     *
     * @param startDate start date
     * @param endDate   end date
     * @return populated ReportResponse
     */
    ReportResponse generateBillingReport(LocalDate startDate, LocalDate endDate);

    /**
     * Generates an organization-focused report.
     *
     * @param organizationId optional organization ID filter
     * @return populated ReportResponse
     */
    ReportResponse generateOrganizationReport(Long organizationId);
}
