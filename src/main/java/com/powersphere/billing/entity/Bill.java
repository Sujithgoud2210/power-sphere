package com.powersphere.billing.entity;

import com.powersphere.billing.enums.BillStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Core entity representing an electricity bill generated for a consumer.
 * Contains meter readings, consumption details, and calculated charges.
 */
@Entity
@Table(name = "bills", indexes = {
    @Index(name = "idx_bill_number", columnList = "billNumber", unique = true),
    @Index(name = "idx_bill_meter", columnList = "meterId"),
    @Index(name = "idx_bill_organization", columnList = "organizationId"),
    @Index(name = "idx_bill_status", columnList = "status"),
    @Index(name = "idx_bill_month_year", columnList = "billingMonth, billingYear"),
    @Index(name = "idx_bill_due_date", columnList = "dueDate")
})
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_number", nullable = false, unique = true, length = 50)
    private String billNumber;

    @Column(name = "meter_id", nullable = false)
    private Long meterId;

    @Column(name = "meter_number", length = 100)
    private String meterNumber;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "organization_name", length = 255)
    private String organizationName;

    @Column(name = "consumer_name", length = 255)
    private String consumerName;

    @Column(name = "consumer_address", length = 500)
    private String consumerAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tariff_plan_id")
    private TariffPlan tariffPlan;

    @Column(name = "billing_month", nullable = false)
    private int billingMonth;

    @Column(name = "billing_year", nullable = false)
    private int billingYear;

    @Column(name = "previous_reading", nullable = false, precision = 12, scale = 2)
    private BigDecimal previousReading;

    @Column(name = "current_reading", nullable = false, precision = 12, scale = 2)
    private BigDecimal currentReading;

    @Column(name = "units_consumed", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitsConsumed;

    @Column(name = "energy_charge", nullable = false, precision = 14, scale = 2)
    private BigDecimal energyCharge;

    @Column(name = "fixed_charge", nullable = false, precision = 12, scale = 2)
    private BigDecimal fixedCharge;

    @Column(name = "service_charge", nullable = false, precision = 12, scale = 2)
    private BigDecimal serviceCharge;

    @Column(name = "subtotal", nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @Column(name = "tax_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "discount", nullable = false, precision = 12, scale = 2)
    private BigDecimal discount;

    @Column(name = "discount_description", length = 255)
    private String discountDescription;

    @Column(name = "late_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal lateFee;

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "amount_paid", precision = 14, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "balance_due", precision = 14, scale = 2)
    private BigDecimal balanceDue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BillStatus status;

    @Column(name = "generated_date", nullable = false)
    private LocalDate generatedDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillItem> billItems = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BillHistory> billHistories = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Builder ---

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final Bill bill = new Bill();

        public Builder id(Long id) { bill.id = id; return this; }
        public Builder billNumber(String billNumber) { bill.billNumber = billNumber; return this; }
        public Builder meterId(Long meterId) { bill.meterId = meterId; return this; }
        public Builder meterNumber(String meterNumber) { bill.meterNumber = meterNumber; return this; }
        public Builder organizationId(Long organizationId) { bill.organizationId = organizationId; return this; }
        public Builder organizationName(String organizationName) { bill.organizationName = organizationName; return this; }
        public Builder consumerName(String consumerName) { bill.consumerName = consumerName; return this; }
        public Builder consumerAddress(String consumerAddress) { bill.consumerAddress = consumerAddress; return this; }
        public Builder tariffPlan(TariffPlan tariffPlan) { bill.tariffPlan = tariffPlan; return this; }
        public Builder billingMonth(int billingMonth) { bill.billingMonth = billingMonth; return this; }
        public Builder billingYear(int billingYear) { bill.billingYear = billingYear; return this; }
        public Builder previousReading(BigDecimal previousReading) { bill.previousReading = previousReading; return this; }
        public Builder currentReading(BigDecimal currentReading) { bill.currentReading = currentReading; return this; }
        public Builder unitsConsumed(BigDecimal unitsConsumed) { bill.unitsConsumed = unitsConsumed; return this; }
        public Builder energyCharge(BigDecimal energyCharge) { bill.energyCharge = energyCharge; return this; }
        public Builder fixedCharge(BigDecimal fixedCharge) { bill.fixedCharge = fixedCharge; return this; }
        public Builder serviceCharge(BigDecimal serviceCharge) { bill.serviceCharge = serviceCharge; return this; }
        public Builder subtotal(BigDecimal subtotal) { bill.subtotal = subtotal; return this; }
        public Builder taxPercentage(BigDecimal taxPercentage) { bill.taxPercentage = taxPercentage; return this; }
        public Builder taxAmount(BigDecimal taxAmount) { bill.taxAmount = taxAmount; return this; }
        public Builder discount(BigDecimal discount) { bill.discount = discount; return this; }
        public Builder discountDescription(String discountDescription) { bill.discountDescription = discountDescription; return this; }
        public Builder lateFee(BigDecimal lateFee) { bill.lateFee = lateFee; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { bill.totalAmount = totalAmount; return this; }
        public Builder amountPaid(BigDecimal amountPaid) { bill.amountPaid = amountPaid; return this; }
        public Builder balanceDue(BigDecimal balanceDue) { bill.balanceDue = balanceDue; return this; }
        public Builder status(BillStatus status) { bill.status = status; return this; }
        public Builder generatedDate(LocalDate generatedDate) { bill.generatedDate = generatedDate; return this; }
        public Builder dueDate(LocalDate dueDate) { bill.dueDate = dueDate; return this; }
        public Builder paidDate(LocalDate paidDate) { bill.paidDate = paidDate; return this; }
        public Builder remarks(String remarks) { bill.remarks = remarks; return this; }

        public Bill build() {
            Objects.requireNonNull(bill.billNumber, "billNumber must not be null");
            Objects.requireNonNull(bill.meterId, "meterId must not be null");
            Objects.requireNonNull(bill.previousReading, "previousReading must not be null");
            Objects.requireNonNull(bill.currentReading, "currentReading must not be null");
            Objects.requireNonNull(bill.unitsConsumed, "unitsConsumed must not be null");
            Objects.requireNonNull(bill.totalAmount, "totalAmount must not be null");
            Objects.requireNonNull(bill.status, "status must not be null");
            Objects.requireNonNull(bill.generatedDate, "generatedDate must not be null");
            Objects.requireNonNull(bill.dueDate, "dueDate must not be null");
            return bill;
        }
    }

    // --- Domain Methods ---

    public void addBillItem(BillItem item) {
        billItems.add(item);
        item.setBill(this);
    }

    public void addBillHistory(BillHistory history) {
        billHistories.add(history);
        history.setBill(this);
    }

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

    public TariffPlan getTariffPlan() { return tariffPlan; }
    public void setTariffPlan(TariffPlan tariffPlan) { this.tariffPlan = tariffPlan; }

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

    public List<BillItem> getBillItems() { return billItems; }
    public void setBillItems(List<BillItem> billItems) { this.billItems = billItems; }

    public List<BillHistory> getBillHistories() { return billHistories; }
    public void setBillHistories(List<BillHistory> billHistories) { this.billHistories = billHistories; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(id, bill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Bill.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("billNumber='" + billNumber + "'")
            .add("meterId=" + meterId)
            .add("billingMonth=" + billingMonth)
            .add("billingYear=" + billingYear)
            .add("unitsConsumed=" + unitsConsumed)
            .add("totalAmount=" + totalAmount)
            .add("status=" + status)
            .toString();
    }
}
