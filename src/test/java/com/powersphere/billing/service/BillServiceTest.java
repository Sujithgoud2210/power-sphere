package com.powersphere.billing.service;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.dto.response.BillResponse;
import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.TariffPlan;
import com.powersphere.billing.enums.BillStatus;
import com.powersphere.billing.enums.ConsumerType;
import com.powersphere.billing.mapper.BillMapper;
import com.powersphere.billing.mapper.TariffPlanMapper;
import com.powersphere.billing.repository.BillRepository;
import com.powersphere.billing.repository.TariffPlanRepository;
import com.powersphere.billing.service.impl.BillServiceImpl;
import com.powersphere.billing.util.BillingCalculator;
import com.powersphere.billing.validation.BillValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link BillServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private TariffPlanRepository tariffPlanRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private BillMapper billMapper;
    private BillValidator billValidator;
    private BillingCalculator billingCalculator;
    private BillServiceImpl billService;

    @BeforeEach
    void setUp() {
        billMapper = Mappers.getMapper(BillMapper.class);
        billValidator = new BillValidator();
        billingCalculator = new BillingCalculator();
        billService = new BillServiceImpl(
                billRepository, tariffPlanRepository,
                billMapper, billValidator,
                billingCalculator, eventPublisher);
    }

    @Test
    void shouldThrowExceptionWhenNoTariffPlanFound() {
        // Given
        GenerateBillRequest request = createValidRequest();

        when(tariffPlanRepository.findByPlanCode(request.getTariffPlanCode()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> billService.generateBill(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tariff plan not found");
    }

    @Test
    void shouldThrowExceptionWhenBillAlreadyExistsForPeriod() {
        // Given
        GenerateBillRequest request = createValidRequest();
        TariffPlan tariffPlan = createTariffPlan();

        when(tariffPlanRepository.findByPlanCode(request.getTariffPlanCode()))
                .thenReturn(Optional.of(tariffPlan));
        when(billRepository.countByMeterIdAndBillingMonthAndBillingYear(
                request.getMeterId(), request.getBillingMonth(), request.getBillingYear()))
                .thenReturn(1L);

        // When & Then
        assertThatThrownBy(() -> billService.generateBill(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    private GenerateBillRequest createValidRequest() {
        GenerateBillRequest request = new GenerateBillRequest();
        request.setMeterId(1L);
        request.setMeterNumber("MTR-001");
        request.setConsumerName("John Doe");
        request.setPreviousReading(new BigDecimal("1000"));
        request.setCurrentReading(new BigDecimal("1500"));
        request.setBillingMonth(6);
        request.setBillingYear(2026);
        request.setTariffPlanCode("RES_STD");
        request.setDueDate(LocalDate.now().plusDays(30));
        return request;
    }

    private TariffPlan createTariffPlan() {
        return TariffPlan.builder()
                .id(1L)
                .planName("Residential Standard")
                .planCode("RES_STD")
                .consumerType(ConsumerType.RESIDENTIAL)
                .fixedCharge(new BigDecimal("100"))
                .energyChargePerUnit(new BigDecimal("7.50"))
                .taxPercentage(new BigDecimal("12"))
                .serviceCharge(new BigDecimal("50"))
                .effectiveFrom(LocalDate.of(2026, 1, 1))
                .build();
    }
}
