package com.powersphere.dashboard.dto.response;

import java.math.BigDecimal;

public class DashboardResponse {

    private long totalOrganizations;
    private long totalUsers;
    private long activeUsers;
    private long totalSmartMeters;
    private long activeSmartMeters;
    private long inactiveSmartMeters;
    private double todaysEnergyConsumption;
    private double monthlyEnergyConsumption;
    private double yearlyEnergyConsumption;
    private BigDecimal todaysRevenue;
    private BigDecimal monthlyRevenue;
    private long pendingBills;
    private long paidBills;
    private long overdueBills;
    private long notificationsSentToday;
    private long unreadNotifications;

    public DashboardResponse() {
        this.todaysRevenue = BigDecimal.ZERO;
        this.monthlyRevenue = BigDecimal.ZERO;
    }

    public long getTotalOrganizations() {
        return totalOrganizations;
    }

    public void setTotalOrganizations(long totalOrganizations) {
        this.totalOrganizations = totalOrganizations;
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

    public long getTotalSmartMeters() {
        return totalSmartMeters;
    }

    public void setTotalSmartMeters(long totalSmartMeters) {
        this.totalSmartMeters = totalSmartMeters;
    }

    public long getActiveSmartMeters() {
        return activeSmartMeters;
    }

    public void setActiveSmartMeters(long activeSmartMeters) {
        this.activeSmartMeters = activeSmartMeters;
    }

    public long getInactiveSmartMeters() {
        return inactiveSmartMeters;
    }

    public void setInactiveSmartMeters(long inactiveSmartMeters) {
        this.inactiveSmartMeters = inactiveSmartMeters;
    }

    public double getTodaysEnergyConsumption() {
        return todaysEnergyConsumption;
    }

    public void setTodaysEnergyConsumption(double todaysEnergyConsumption) {
        this.todaysEnergyConsumption = todaysEnergyConsumption;
    }

    public double getMonthlyEnergyConsumption() {
        return monthlyEnergyConsumption;
    }

    public void setMonthlyEnergyConsumption(double monthlyEnergyConsumption) {
        this.monthlyEnergyConsumption = monthlyEnergyConsumption;
    }

    public double getYearlyEnergyConsumption() {
        return yearlyEnergyConsumption;
    }

    public void setYearlyEnergyConsumption(double yearlyEnergyConsumption) {
        this.yearlyEnergyConsumption = yearlyEnergyConsumption;
    }

    public BigDecimal getTodaysRevenue() {
        return todaysRevenue;
    }

    public void setTodaysRevenue(BigDecimal todaysRevenue) {
        this.todaysRevenue = todaysRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public long getPendingBills() {
        return pendingBills;
    }

    public void setPendingBills(long pendingBills) {
        this.pendingBills = pendingBills;
    }

    public long getPaidBills() {
        return paidBills;
    }

    public void setPaidBills(long paidBills) {
        this.paidBills = paidBills;
    }

    public long getOverdueBills() {
        return overdueBills;
    }

    public void setOverdueBills(long overdueBills) {
        this.overdueBills = overdueBills;
    }

    public long getNotificationsSentToday() {
        return notificationsSentToday;
    }

    public void setNotificationsSentToday(long notificationsSentToday) {
        this.notificationsSentToday = notificationsSentToday;
    }

    public long getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(long unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }
}
