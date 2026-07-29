package com.powersphere.billing.dto.response;

import com.powersphere.billing.enums.ConsumerType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for tariff plan data.
 */
public class TariffPlanResponse {

    private Long id;
    private String planName;
    private String planCode;
    private ConsumerType consumerType;
    private BigDecimal fixedCharge;
    private BigDecimal energyChargePerUnit;
    private BigDecimal taxPercentage;
    private BigDecimal serviceCharge;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
}
