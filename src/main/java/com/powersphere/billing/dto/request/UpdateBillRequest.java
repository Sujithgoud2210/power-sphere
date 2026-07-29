package com.powersphere.billing.dto.request;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing bill's modifiable fields.
 */
public class UpdateBillRequest {

    private String remarks;
    private BigDecimal discount;
    private String discountDescription;
    private BigDecimal lateFee;

    // --- Getters and Setters ---

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public String getDiscountDescription() { return discountDescription; }
    public void setDiscountDescription(String discountDescription) { this.discountDescription = discountDescription; }

    public BigDecimal getLateFee() { return lateFee; }
    public void setLateFee(BigDecimal lateFee) { this.lateFee = lateFee; }
}
