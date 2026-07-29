package com.powersphere.billing.util;

import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.TariffPlan;
import com.powersphere.billing.enums.ConsumerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link BillingCalculator}.
 */
class BillingCalculatorTest {

    private BillingCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new BillingCalculator();
    }

    @Test
    void shouldCalculateUnitsConsumed() {
        BigDecimal units = calculator.calculateUnitsConsumed(
                new BigDecimal("1500"), new BigDecimal("1000"));
        assertThat(units).isEqualByComparingTo(new BigDecimal("500"));
    }

    @Test
    void shouldCalculateEnergyCharge() {
        BigDecimal charge = calculator.calculateEnergyCharge(
                new BigDecimal("500"), new BigDecimal("7.50"));
        assertThat(charge).isEqualByComparingTo(new BigDecimal("3750.00"));
    }

    @Test
    void shouldCalculateSubtotal() {
        BigDecimal subtotal = calculator.calculateSubtotal(
                new BigDecimal("3750"), new BigDecimal("100"), new BigDecimal("50"));
        assertThat(subtotal).isEqualByComparingTo(new BigDecimal("3900.00"));
    }

    @Test
    void shouldCalculateTax() {
        BigDecimal tax = calculator.calculateTax(
                new BigDecimal("3900"), new BigDecimal("12.00"));
        assertThat(tax).isEqualByComparingTo(new BigDecimal("468.00"));
    }

    @Test
    void shouldCalculateTotal() {
        BigDecimal total = calculator.calculateTotal(
                new BigDecimal("3900"), new BigDecimal("468"),
                new BigDecimal("100"), new BigDecimal("50"));
        assertThat(total).isEqualByComparingTo(new BigDecimal("4318.00"));
    }

    @Test
    void shouldPopulateBillWithAllCharges() {
        // Given
        Bill bill = Bill.builder()
                .meterId(1L)
                .previousReading(new BigDecimal("1000"))
                .currentReading(new BigDecimal("1500"))
                .build();

        TariffPlan tariff = TariffPlan.builder()
                .planName("Residential Standard")
                .planCode("RES_STD")
                .consumerType(ConsumerType.RESIDENTIAL)
                .fixedCharge(new BigDecimal("100.00"))
                .energyChargePerUnit(new BigDecimal("7.50"))
                .taxPercentage(new BigDecimal("12.00"))
                .serviceCharge(new BigDecimal("50.00"))
                .effectiveFrom(LocalDate.of(2026, 1, 1))
                .build();

        // When
        calculator.calculateBillCharges(bill, tariff);

        // Then
        assertThat(bill.getUnitsConsumed()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(bill.getEnergyCharge()).isEqualByComparingTo(new BigDecimal("3750.00"));
        assertThat(bill.getFixedCharge()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(bill.getServiceCharge()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(bill.getSubtotal()).isEqualByComparingTo(new BigDecimal("3900.00"));
        assertThat(bill.getTaxPercentage()).isEqualByComparingTo(new BigDecimal("12.00"));
        assertThat(bill.getTaxAmount()).isEqualByComparingTo(new BigDecimal("468.00"));
        assertThat(bill.getDiscount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bill.getLateFee()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(bill.getTotalAmount()).isEqualByComparingTo(new BigDecimal("4368.00"));
        assertThat(bill.getBalanceDue()).isEqualByComparingTo(new BigDecimal("4368.00"));

        // Verify bill items were created
        assertThat(bill.getBillItems()).hasSize(4);
        assertThat(bill.getBillItems().get(0).getItemType()).isEqualTo("ENERGY_CHARGE");
        assertThat(bill.getBillItems().get(1).getItemType()).isEqualTo("FIXED_CHARGE");
        assertThat(bill.getBillItems().get(2).getItemType()).isEqualTo("SERVICE_CHARGE");
        assertThat(bill.getBillItems().get(3).getItemType()).isEqualTo("TAX");
    }
}
