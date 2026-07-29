package com.powersphere.energy.service.impl;

import com.powersphere.energy.dto.response.ConsumptionResponse;
import com.powersphere.energy.entity.EnergyReading;
import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingSource;
import com.powersphere.energy.enums.ReadingType;
import com.powersphere.energy.mapper.EnergyReadingMapper;
import com.powersphere.energy.repository.EnergyReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ConsumptionServiceImplTest {

    @Mock
    private EnergyReadingRepository repository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private ConsumptionServiceImpl consumptionService;

    @BeforeEach
    void setUp() {
        EnergyReadingMapper mapper = Mappers.getMapper(EnergyReadingMapper.class);
        doNothing().when(eventPublisher).publishEvent(any());
        consumptionService = new ConsumptionServiceImpl(repository, mapper, eventPublisher);
    }

    @Test
    void shouldCalculateCorrectConsumption() {
        EnergyReading reading = EnergyReading.builder()
                .id(1L)
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .consumption(new BigDecimal("500.000"))
                .readingSource(ReadingSource.WEB)
                .qualityStatus(QualityStatus.VALID)
                .active(true)
                .build();

        ConsumptionResponse response = consumptionService.calculateConsumption(reading);

        assertThat(response).isNotNull();
        assertThat(response.getMeterId()).isEqualTo(1L);
        assertThat(response.getPreviousReading()).isEqualByComparingTo(new BigDecimal("1000.000"));
        assertThat(response.getCurrentReading()).isEqualByComparingTo(new BigDecimal("1500.000"));
        assertThat(response.getConsumption()).isEqualByComparingTo(new BigDecimal("500.000"));
        assertThat(response.getPercentageChange()).isEqualByComparingTo(new BigDecimal("50.00"));
        assertThat(response.getUnit()).isEqualTo("kWh");
    }

    @Test
    void shouldHandleZeroConsumption() {
        EnergyReading reading = EnergyReading.builder()
                .id(2L)
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1000.000"))
                .consumption(BigDecimal.ZERO)
                .readingSource(ReadingSource.WEB)
                .qualityStatus(QualityStatus.VALID)
                .active(true)
                .build();

        ConsumptionResponse response = consumptionService.calculateConsumption(reading);

        assertThat(response).isNotNull();
        assertThat(response.getConsumption()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getPercentageChange()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldProduceConsumptionHistory() {
        EnergyReading reading1 = EnergyReading.builder()
                .id(1L)
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now().minusDays(1))
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .consumption(new BigDecimal("500.000"))
                .readingSource(ReadingSource.WEB)
                .qualityStatus(QualityStatus.VALID)
                .active(true)
                .build();

        EnergyReading reading2 = EnergyReading.builder()
                .id(2L)
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1500.000"))
                .currentReading(new BigDecimal("2000.000"))
                .consumption(new BigDecimal("500.000"))
                .readingSource(ReadingSource.WEB)
                .qualityStatus(QualityStatus.VALID)
                .active(true)
                .build();

        List<ConsumptionResponse> history = List.of(
                consumptionService.calculateConsumption(reading1),
                consumptionService.calculateConsumption(reading2)
        );

        assertThat(history).hasSize(2);
        assertThat(history.get(0).getConsumption()).isEqualByComparingTo(new BigDecimal("500.000"));
        assertThat(history.get(1).getConsumption()).isEqualByComparingTo(new BigDecimal("500.000"));
    }
}
