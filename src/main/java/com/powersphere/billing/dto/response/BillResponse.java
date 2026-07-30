package com.powersphere.billing.dto.response;

import com.powersphere.billing.enums.BillStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO representing a complete bill with all details.
 */
public class BillResponse {

    private Long id;
    private String billNumber;
    private Long meterId;
    private String meterNumber;
    private Long organizationId;
    private String organizationName;
    private String consumerName;
    private String consumerAddress;
    private TariffPlanResponse tariffPlan;
    private int billingMonth;
    private int billingYear;
    private BigDecimal previousReading;
    private BigDecimal currentReading;
    private BigDecimal unitsConsumed;
    private BigDecimal energyCharge;
    private BigDecimal fixedCharge;
    private BigDecimal serviceCharge;
    private BigDecimal subtotal;
    private BigDecimal taxPercentage;
    private BigDecimal taxAmount;
    private BigDecimal discount;
    private String discountDescription;
    private BigDecimal lateFee;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal balanceDue;
    private BillStatus status;
    private LocalDate generatedDate;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private String remarks;
    private List<BillItemResponse> billItems;
    private List<BillHistoryResponse> billHistories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

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

    public TariffPlanResponse getTariffPlan() { return tariffPlan; }
    public void setTariffPlan(TariffPlanResponse tariffPlan) { this.tariffPlan = tariffPlan; }

    public int getBillingMonth() { return billingMonth; }
    public void setBillingMonth(int billingMonth) { this.billingMonth = billingMonth; }

    public int getBillingYear() { return billingYear; }
    public void setBillingYear(int billingYear) { this.billingYear = billingYear; }

    public BigDecimal getPreviousReading() { return previousReading; }
    public void setPreviousReading(BigDecimal previousReading) { this.previousReading = previousReading; }

    public BigDecimal getCurrentReading() { return currentReading; }
    public void setCurrentReading(BigDecimal currentReading) { this.currentReading = currentReading; }

    public BigDecimal getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(BigDecimal unitsConsumed) { this.unitsConsumed = unitsConsumed; }

    public BigDecimal getEnergyCharge() { return energyCharge; }
    public void setEnergyCharge(BigDecimal energyCharge) { this.energyCharge = energyCharge; }

    public BigDecimal getFixedCharge() { return fixedCharge; }
    public void setFixedCharge(BigDecimal fixedCharge) { this.fixedCharge = fixedCharge; }

    public BigDecimal getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(BigDecimal serviceCharge) { this.serviceCharge = serviceCharge; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTaxPercentage() { return taxPercentage; }
    public void setTaxPercentage(BigDecimal taxPercentage) { this.taxPercentage = taxPercentage; }

    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public String getDiscountDescription() { return discountDescription; }
    public void setDiscountDescription(String discountDescription) { this.discountDescription = discountDescription; }

    public BigDecimal getLateFee() { return lateFee; }
    public void setLateFee(BigDecimal lateFee) { this.lateFee = lateFee; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public BigDecimal getBalanceDue() { return balanceDue; }
    public void setBalanceDue(BigDecimal balanceDue) { this.balanceDue = balanceDue; }

    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }

    public LocalDate getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDate generatedDate) { this.generatedDate = generatedDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public List<BillItemResponse> getBillItems() { return billItems; }
    public void setBillItems(List<BillItemResponse> billItems) { this.billItems = billItems; }

    public List<BillHistoryResponse> getBillHistories() { return billHistories; }
    public void setBillHistories(List<BillHistoryResponse> billHistories) { this.billHistories = billHistories; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
