package com.powersphere.report.dto;

import com.powersphere.dashboard.dto.response.ConsumptionTrendResponse;
import com.powersphere.dashboard.dto.response.DashboardResponse;
import com.powersphere.dashboard.dto.response.OrganizationSummaryResponse;
import com.powersphere.dashboard.dto.response.RevenueTrendResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportResponse {

    private String reportType;
    private LocalDate generatedDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private DashboardResponse summary;
    private ConsumptionTrendResponse consumptionTrends;
    private RevenueTrendResponse revenueTrends;
    private List<OrganizationSummaryResponse> organizationSummaries;
    private Map<String, Long> meterStatusDistribution;
    private Map<String, Long> billStatusDistribution;
    private List<OrganizationSummaryResponse> topConsumers;
    private List<OrganizationSummaryResponse> topOrganizations;
    private List<Integer> peakConsumptionHours;

    public ReportResponse() {
        this.generatedDate = LocalDate.now();
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public DashboardResponse getSummary() {
        return summary;
    }

    public void setSummary(DashboardResponse summary) {
        this.summary = summary;
    }

    public ConsumptionTrendResponse getConsumptionTrends() {
        return consumptionTrends;
    }

    public void setConsumptionTrends(ConsumptionTrendResponse consumptionTrends) {
        this.consumptionTrends = consumptionTrends;
    }

    public RevenueTrendResponse getRevenueTrends() {
        return revenueTrends;
    }

    public void setRevenueTrends(RevenueTrendResponse revenueTrends) {
        this.revenueTrends = revenueTrends;
    }

    public List<OrganizationSummaryResponse> getOrganizationSummaries() {
        return organizationSummaries;
    }

    public void setOrganizationSummaries(List<OrganizationSummaryResponse> organizationSummaries) {
        this.organizationSummaries = organizationSummaries;
    }

    public Map<String, Long> getMeterStatusDistribution() {
        return meterStatusDistribution;
    }

    public void setMeterStatusDistribution(Map<String, Long> meterStatusDistribution) {
        this.meterStatusDistribution = meterStatusDistribution;
    }

    public Map<String, Long> getBillStatusDistribution() {
        return billStatusDistribution;
    }

    public void setBillStatusDistribution(Map<String, Long> billStatusDistribution) {
        this.billStatusDistribution = billStatusDistribution;
    }

    public List<OrganizationSummaryResponse> getTopConsumers() {
        return topConsumers;
    }

    public void setTopConsumers(List<OrganizationSummaryResponse> topConsumers) {
        this.topConsumers = topConsumers;
    }

    public List<OrganizationSummaryResponse> getTopOrganizations() {
        return topOrganizations;
    }

    public void setTopOrganizations(List<OrganizationSummaryResponse> topOrganizations) {
        this.topOrganizations = topOrganizations;
    }

    public List<Integer> getPeakConsumptionHours() {
        return peakConsumptionHours;
    }

    public void setPeakConsumptionHours(List<Integer> peakConsumptionHours) {
        this.peakConsumptionHours = peakConsumptionHours;
    }
}
