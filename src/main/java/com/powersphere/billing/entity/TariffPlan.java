package com.powersphere.billing.entity;

import com.powersphere.billing.enums.ConsumerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Entity representing an electricity tariff plan. Defines the rate structure
 * for different consumer types including fixed charges, energy charges per unit,
 * taxes, and service charges.
 */
@Entity
@Table(name = "tariff_plans", indexes = {
    @Index(name = "idx_tariff_plan_code", columnList = "planCode", unique = true),
    @Index(name = "idx_tariff_consumer_type", columnList = "consumerType"),
    @Index(name = "idx_tariff_active", columnList = "active")
})
public class TariffPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_name", nullable = false, length = 255)
    private String planName;

    @Column(name = "plan_code", nullable = false, unique = true, length = 50)
    private String planCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "consumer_type", nullable = false, length = 20)
    private ConsumerType consumerType;

    @Column(name = "fixed_charge", nullable = false, precision = 12, scale = 2)
    private BigDecimal fixedCharge;

    @Column(name = "energy_charge_per_unit", nullable = false, precision = 12, scale = 4)
    private BigDecimal energyChargePerUnit;

    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @Column(name = "service_charge", nullable = false, precision = 12, scale = 2)
    private BigDecimal serviceCharge;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "active", nullable = false)
    private boolean active = true;

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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final TariffPlan plan = new TariffPlan();

        public Builder id(Long id) { plan.id = id; return this; }
        public Builder planName(String planName) { plan.planName = planName; return this; }
        public Builder planCode(String planCode) { plan.planCode = planCode; return this; }
        public Builder consumerType(ConsumerType consumerType) { plan.consumerType = consumerType; return this; }
        public Builder fixedCharge(BigDecimal fixedCharge) { plan.fixedCharge = fixedCharge; return this; }
        public Builder energyChargePerUnit(BigDecimal energyChargePerUnit) { plan.energyChargePerUnit = energyChargePerUnit; return this; }
        public Builder taxPercentage(BigDecimal taxPercentage) { plan.taxPercentage = taxPercentage; return this; }
        public Builder serviceCharge(BigDecimal serviceCharge) { plan.serviceCharge = serviceCharge; return this; }
        public Builder effectiveFrom(LocalDate effectiveFrom) { plan.effectiveFrom = effectiveFrom; return this; }
        public Builder effectiveTo(LocalDate effectiveTo) { plan.effectiveTo = effectiveTo; return this; }
        public Builder active(boolean active) { plan.active = active; return this; }

        public TariffPlan build() {
            Objects.requireNonNull(plan.planName, "planName must not be null");
            Objects.requireNonNull(plan.planCode, "planCode must not be null");
            Objects.requireNonNull(plan.consumerType, "consumerType must not be null");
            Objects.requireNonNull(plan.fixedCharge, "fixedCharge must not be null");
            Objects.requireNonNull(plan.energyChargePerUnit, "energyChargePerUnit must not be null");
            Objects.requireNonNull(plan.taxPercentage, "taxPercentage must not be null");
            Objects.requireNonNull(plan.serviceCharge, "serviceCharge must not be null");
            Objects.requireNonNull(plan.effectiveFrom, "effectiveFrom must not be null");
            return plan;
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getPlanCode() { return planCode; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }

    public ConsumerType getConsumerType() { return consumerType; }
    public void setConsumerType(ConsumerType consumerType) { this.consumerType = consumerType; }

    public BigDecimal getFixedCharge() { return fixedCharge; }
    public void setFixedCharge(BigDecimal fixedCharge) { this.fixedCharge = fixedCharge; }

    public BigDecimal getEnergyChargePerUnit() { return energyChargePerUnit; }
    public void setEnergyChargePerUnit(BigDecimal energyChargePerUnit) { this.energyChargePerUnit = energyChargePerUnit; }

    public BigDecimal getTaxPercentage() { return taxPercentage; }
    public void setTaxPercentage(BigDecimal taxPercentage) { this.taxPercentage = taxPercentage; }

    public BigDecimal getServiceCharge() { return serviceCharge; }
    public void setServiceCharge(BigDecimal serviceCharge) { this.serviceCharge = serviceCharge; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TariffPlan that = (TariffPlan) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TariffPlan.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("planCode='" + planCode + "'")
            .add("consumerType=" + consumerType)
            .add("active=" + active)
            .toString();
    }
}
