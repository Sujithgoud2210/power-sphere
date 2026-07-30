package com.powersphere.energy.mapper;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.dto.response.EnergyReadingResponse;
import com.powersphere.energy.entity.EnergyReading;
import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingSource;
import com.powersphere.energy.enums.ReadingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EnergyReadingMapperTest {

    private EnergyReadingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EnergyReadingMapper.class);
    }

    @Test
    void shouldMapRequestToEntity() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .voltage(new BigDecimal("230.000"))
                .current(new BigDecimal("15.000"))
                .powerFactor(new BigDecimal("0.950"))
                .frequency(new BigDecimal("50.000"))
                .power(new BigDecimal("3500.000"))
                .temperature(new BigDecimal("25.00"))
                .batteryLevel(new BigDecimal("85.00"))
                .signalStrength(new BigDecimal("4.5"))
                .readingSource(ReadingSource.WEB)
                .remarks("Test reading")
                .build();

        EnergyReading entity = mapper.toEntity(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getMeterId()).isEqualTo(1L);
        assertThat(entity.getReadingType()).isEqualTo(ReadingType.AUTOMATIC);
        assertThat(entity.getPreviousReading()).isEqualByComparingTo(new BigDecimal("1000.000"));
        assertThat(entity.getCurrentReading()).isEqualByComparingTo(new BigDecimal("1500.000"));
        assertThat(entity.getVoltage()).isEqualByComparingTo(new BigDecimal("230.000"));
        assertThat(entity.getReadingSource()).isEqualTo(ReadingSource.WEB);
        assertThat(entity.getRemarks()).isEqualTo("Test reading");
    }

    @Test
    void shouldMapEntityToResponse() {
        EnergyReading entity = EnergyReading.builder()
                .id(1L)
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .consumption(new BigDecimal("500.000"))
                .voltage(new BigDecimal("230.000"))
                .current(new BigDecimal("15.000"))
                .powerFactor(new BigDecimal("0.950"))
                .frequency(new BigDecimal("50.000"))
                .power(new BigDecimal("3500.000"))
                .temperature(new BigDecimal("25.00"))
                .batteryLevel(new BigDecimal("85.00"))
                .signalStrength(new BigDecimal("4.5"))
                .readingSource(ReadingSource.WEB)
                .qualityStatus(QualityStatus.VALID)
                .remarks("Test reading")
                .active(true)
                .build();

        EnergyReadingResponse response = mapper.toResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getMeterId()).isEqualTo(1L);
        assertThat(response.getReadingType()).isEqualTo(ReadingType.AUTOMATIC);
        assertThat(response.getPreviousReading()).isEqualByComparingTo(new BigDecimal("1000.000"));
        assertThat(response.getCurrentReading()).isEqualByComparingTo(new BigDecimal("1500.000"));
        assertThat(response.getConsumption()).isEqualByComparingTo(new BigDecimal("500.000"));
        assertThat(response.getQualityStatus()).isEqualTo(QualityStatus.VALID);
        assertThat(response.isActive()).isTrue();
    }
}
