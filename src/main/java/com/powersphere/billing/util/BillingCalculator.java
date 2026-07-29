package com.powersphere.billing.util;

import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.BillItem;
import com.powersphere.billing.entity.TariffPlan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility component for performing billing calculations including energy charge,
 * tax computation, discount application, and late fee calculation.
 */
@Component
public class BillingCalculator {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * Calculates units consumed from meter readings.
     */
    public BigDecimal calculateUnitsConsumed(BigDecimal currentReading, BigDecimal previousReading) {
        return currentReading.subtract(previousReading);
    }

    /**
     * Calculates the energy charge based on units consumed and tariff rate.
     */
    public BigDecimal calculateEnergyCharge(BigDecimal unitsConsumed, BigDecimal ratePerUnit) {
        return unitsConsumed.multiply(ratePerUnit).setScale(SCALE, ROUNDING);
    }

    /**
     * Calculates the subtotal before tax (energy charge + fixed charge + service charge).
     */
    public BigDecimal calculateSubtotal(BigDecimal energyCharge, BigDecimal fixedCharge, BigDecimal serviceCharge) {
        return energyCharge.add(fixedCharge).add(serviceCharge).setScale(SCALE, ROUNDING);
    }

    /**
     * Calculates tax amount from the subtotal and tax percentage.
     */
    public BigDecimal calculateTax(BigDecimal subtotal, BigDecimal taxPercentage) {
        return subtotal.multiply(taxPercentage)
                .divide(HUNDRED, SCALE, ROUNDING);
    }

    /**
     * Calculates the total amount (subtotal + tax - discount + late fee).
     */
    public BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal tax, BigDecimal discount, BigDecimal lateFee) {
        return subtotal.add(tax).subtract(discount).add(lateFee)
                .setScale(SCALE, ROUNDING);
    }

    /**
     * Calculates late fee as a percentage of the total amount.
     */
    public BigDecimal calculateLateFee(BigDecimal totalAmount, BigDecimal lateFeePercentage, int daysOverdue) {
        if (daysOverdue <= 0) {
            return BigDecimal.ZERO.setScale(SCALE, ROUNDING);
        }
        BigDecimal dailyRate = lateFeePercentage.divide(new BigDecimal("100"), 4, ROUNDING);
        BigDecimal monthlyRate = dailyRate.multiply(new BigDecimal("30"));
        return totalAmount.multiply(monthlyRate)
                .setScale(SCALE, ROUNDING);
    }

    /**
     * Calculates all charges for a bill from the given tariff plan and readings.
     * Populates the bill entity with all calculated values.
     *
     * @param bill the bill to populate with calculations
     * @param tariffPlan the applicable tariff plan
     */
    public void calculateBillCharges(Bill bill, TariffPlan tariffPlan) {
        // Calculate units consumed
        BigDecimal unitsConsumed = calculateUnitsConsumed(
                bill.getCurrentReading(), bill.getPreviousReading());
        bill.setUnitsConsumed(unitsConsumed);

        // Apply tariff rates
        bill.setEnergyCharge(calculateEnergyCharge(unitsConsumed, tariffPlan.getEnergyChargePerUnit()));
        bill.setFixedCharge(tariffPlan.getFixedCharge());
        bill.setServiceCharge(tariffPlan.getServiceCharge());

        // Calculate subtotal
        BigDecimal subtotal = calculateSubtotal(
                bill.getEnergyCharge(), bill.getFixedCharge(), bill.getServiceCharge());
        bill.setSubtotal(subtotal);

        // Apply tax
        bill.setTaxPercentage(tariffPlan.getTaxPercentage());
        BigDecimal taxAmount = calculateTax(subtotal, tariffPlan.getTaxPercentage());
        bill.setTaxAmount(taxAmount);

        // Initialize zero values for optional fields
        bill.setDiscount(BigDecimal.ZERO.setScale(SCALE, ROUNDING));
        bill.setLateFee(BigDecimal.ZERO.setScale(SCALE, ROUNDING));

        // Calculate total
        BigDecimal total = calculateTotal(subtotal, taxAmount,
                bill.getDiscount(), bill.getLateFee());
        bill.setTotalAmount(total);

        // Set balance due (initially equals total)
        bill.setBalanceDue(total);

        // Create bill items
        createBillItems(bill, tariffPlan, unitsConsumed);
    }

    /**
     * Creates the individual line items for a bill based on calculated charges.
     */
    private void createBillItems(Bill bill, TariffPlan tariffPlan, BigDecimal unitsConsumed) {
        int seq = 1;

        // Energy charge item
        bill.addBillItem(new BillItem(
                "ENERGY_CHARGE",
                "Energy Charge @ " + tariffPlan.getEnergyChargePerUnit() + " per unit",
                unitsConsumed,
                tariffPlan.getEnergyChargePerUnit(),
                bill.getEnergyCharge(),
                seq++
        ));

        // Fixed charge item
        bill.addBillItem(new BillItem(
                "FIXED_CHARGE",
                "Fixed Charge",
                null, null,
                bill.getFixedCharge(),
                seq++
        ));

        // Service charge item
        bill.addBillItem(new BillItem(
                "SERVICE_CHARGE",
                "Service Charge",
                null, null,
                bill.getServiceCharge(),
                seq++
        ));

        // Tax item
        if (bill.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            bill.addBillItem(new BillItem(
                    "TAX",
                    "Tax @ " + tariffPlan.getTaxPercentage() + "%",
                    null, null,
                    bill.getTaxAmount(),
                    seq++
            ));
        }
    }
}
