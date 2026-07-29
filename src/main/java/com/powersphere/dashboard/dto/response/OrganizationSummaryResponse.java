package com.powersphere.dashboard.dto.response;

import java.math.BigDecimal;

public class OrganizationSummaryResponse {

    private Long organizationId;
    private String organizationName;
    private long totalUsers;
    private long activeUsers;
    private long totalMeters;
    private double totalConsumption;
    private BigDecimal totalRevenue;
    private long totalBills;

    public OrganizationSummaryResponse() {
        this.totalRevenue = BigDecimal.ZERO;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public long getTotalMeters() {
        return totalMeters;
    }

    public void setTotalMeters(long totalMeters) {
        this.totalMeters = totalMeters;
    }

    public double getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(double totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(long totalBills) {
        this.totalBills = totalBills;
    }
}
