package com.powersphere.billing.mapper;

import com.powersphere.billing.dto.response.BillItemResponse;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.BillItem;
import com.powersphere.billing.enums.BillStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link BillMapper}.
 */
class BillMapperTest {

    private BillMapper billMapper;

    @BeforeEach
    void setUp() {
        billMapper = Mappers.getMapper(BillMapper.class);
    }

    @Test
    void shouldMapBillToResponse() {
        // Given
        Bill bill = Bill.builder()
                .id(1L)
                .billNumber("PSP2026070100000001")
                .meterId(1L)
                .meterNumber("MTR-001")
                .consumerName("John Doe")
                .billingMonth(6)
                .billingYear(2026)
                .previousReading(new BigDecimal("1000"))
                .currentReading(new BigDecimal("1500"))
                .unitsConsumed(new BigDecimal("500"))
                .energyCharge(new BigDecimal("3750.00"))
                .fixedCharge(new BigDecimal("100.00"))
                .serviceCharge(new BigDecimal("50.00"))
                .subtotal(new BigDecimal("3900.00"))
                .taxPercentage(new BigDecimal("12.00"))
                .taxAmount(new BigDecimal("468.00"))
                .discount(BigDecimal.ZERO)
                .lateFee(BigDecimal.ZERO)
                .totalAmount(new BigDecimal("4368.00"))
                .balanceDue(new BigDecimal("4368.00"))
                .status(BillStatus.GENERATED)
                .generatedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .build();

        // Add a bill item
        bill.addBillItem(new BillItem(
                "ENERGY_CHARGE", "Energy Charge @ 7.50 per unit",
                new BigDecimal("500"), new BigDecimal("7.50"),
                new BigDecimal("3750.00"), 1));

        // When
        BillResponse response = billMapper.toResponse(bill);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBillNumber()).isEqualTo("PSP2026070100000001");
        assertThat(response.getMeterId()).isEqualTo(1L);
        assertThat(response.getConsumerName()).isEqualTo("John Doe");
        assertThat(response.getUnitsConsumed()).isEqualByComparingTo(new BigDecimal("500"));
        assertThat(response.getEnergyCharge()).isEqualByComparingTo(new BigDecimal("3750.00"));
        assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("4368.00"));
        assertThat(response.getStatus()).isEqualTo(BillStatus.GENERATED);

        // Verify bill items are mapped
        assertThat(response.getBillItems()).hasSize(1);
        assertThat(response.getBillItems().get(0).getItemType()).isEqualTo("ENERGY_CHARGE");
        assertThat(response.getBillItems().get(0).getAmount()).isEqualByComparingTo(new BigDecimal("3750.00"));
    }
}
