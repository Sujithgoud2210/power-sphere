package com.powersphere.billing.validation;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.enums.BillStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link BillValidator}.
 */
class BillValidatorTest {

    private BillValidator validator;

    @BeforeEach
    void setUp() {
        validator = new BillValidator();
    }

    @Test
    void shouldPassValidationForValidRequest() {
        GenerateBillRequest request = createValidRequest();
        List<String> errors = validator.validateGenerateRequest(request);
        assertThat(errors).isEmpty();
    }

    @Test
    void shouldFailWhenMeterIdIsMissing() {
        GenerateBillRequest request = createValidRequest();
        request.setMeterId(null);
        List<String> errors = validator.validateGenerateRequest(request);
        assertThat(errors).contains("Meter ID is required");
    }

    @Test
    void shouldFailWhenCurrentReadingLessThanPrevious() {
        GenerateBillRequest request = createValidRequest();
        request.setPreviousReading(new BigDecimal("1500"));
        request.setCurrentReading(new BigDecimal("1000"));
        List<String> errors = validator.validateGenerateRequest(request);
        assertThat(errors).contains("Current reading must be greater than or equal to previous reading");
    }

    @Test
    void shouldFailWhenBillingMonthIsInvalid() {
        GenerateBillRequest request = createValidRequest();
        request.setBillingMonth(13);
        List<String> errors = validator.validateGenerateRequest(request);
        assertThat(errors).contains("Billing month must be between 1 and 12");
    }

    @Test
    void shouldFailWhenDueDateIsInPast() {
        GenerateBillRequest request = createValidRequest();
        request.setDueDate(LocalDate.now().minusDays(1));
        List<String> errors = validator.validateGenerateRequest(request);
        assertThat(errors).contains("Due date must not be in the past");
    }

    @Test
    void shouldRejectCancellingPaidBill() {
        Bill bill = createBill(BillStatus.PAID);
        assertThatThrownBy(() -> validator.validateStatusTransition(bill, BillStatus.CANCELLED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot cancel a paid bill");
    }

    @Test
    void shouldAllowCancellingGeneratedBill() {
        Bill bill = createBill(BillStatus.GENERATED);
        // Should not throw
        validator.validateStatusTransition(bill, BillStatus.CANCELLED);
    }

    private GenerateBillRequest createValidRequest() {
        GenerateBillRequest request = new GenerateBillRequest();
        request.setMeterId(1L);
        request.setPreviousReading(new BigDecimal("1000"));
        request.setCurrentReading(new BigDecimal("1500"));
        request.setBillingMonth(6);
        request.setBillingYear(2026);
        request.setDueDate(LocalDate.now().plusDays(30));
        return request;
    }

    private Bill createBill(BillStatus status) {
        return Bill.builder()
                .billNumber("PSP001")
                .meterId(1L)
                .previousReading(new BigDecimal("1000"))
                .currentReading(new BigDecimal("1500"))
                .unitsConsumed(new BigDecimal("500"))
                .energyCharge(new BigDecimal("3500"))
                .fixedCharge(new BigDecimal("100"))
                .serviceCharge(new BigDecimal("50"))
                .subtotal(new BigDecimal("3650"))
                .taxAmount(new BigDecimal("438"))
                .discount(BigDecimal.ZERO)
                .lateFee(BigDecimal.ZERO)
                .totalAmount(new BigDecimal("4088"))
                .status(status)
                .generatedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .build();
    }
}
