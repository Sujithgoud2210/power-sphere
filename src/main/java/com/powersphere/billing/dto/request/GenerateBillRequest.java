package com.powersphere.billing.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for generating a new electricity bill from meter readings.
 */
public class GenerateBillRequest {

    private Long meterId;
    private String meterNumber;
    private Long organizationId;
    private String organizationName;
    private String consumerName;
    private String consumerAddress;
    private String tariffPlanCode;
    private int billingMonth;
    private int billingYear;
    private BigDecimal previousReading;
    private BigDecimal currentReading;
    private LocalDate dueDate;
    private String remarks;

    // --- Getters and Setters ---

    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }

    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getConsumerName() { return consumerName; }
    public void setConsumerName(String consumerName) { this.consumerName = consumerName; }

    public String getConsumerAddress() { return consumerAddress; }
    public void setConsumerAddress(String consumerAddress) { this.consumerAddress = consumerAddress; }

    public String getTariffPlanCode() { return tariffPlanCode; }
    public void setTariffPlanCode(String tariffPlanCode) { this.tariffPlanCode = tariffPlanCode; }

    public int getBillingMonth() { return billingMonth; }
    public void setBillingMonth(int billingMonth) { this.billingMonth = billingMonth; }

    public int getBillingYear() { return billingYear; }
    public void setBillingYear(int billingYear) { this.billingYear = billingYear; }

    public BigDecimal getPreviousReading() { return previousReading; }
    public void setPreviousReading(BigDecimal previousReading) { this.previousReading = previousReading; }

    public BigDecimal getCurrentReading() { return currentReading; }
    public void setCurrentReading(BigDecimal currentReading) { this.currentReading = currentReading; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
