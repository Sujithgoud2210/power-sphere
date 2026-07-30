package com.powersphere.energy.validation;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.exception.DuplicateReadingException;
import com.powersphere.energy.exception.InvalidReadingException;
import com.powersphere.energy.repository.EnergyReadingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnergyReadingValidatorTest {

    @Mock
    private EnergyReadingRepository repository;

    private EnergyReadingValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EnergyReadingValidator(repository);
    }

    @Test
    void shouldRejectCurrentReadingLessThanPreviousReading() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(com.powersphere.energy.enums.ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1500.000"))
                .currentReading(new BigDecimal("1000.000"))
                .readingSource(com.powersphere.energy.enums.ReadingSource.WEB)
                .build();

        assertThatThrownBy(() -> validator.validateNewReading(request))
                .isInstanceOf(InvalidReadingException.class)
                .hasMessageContaining("cannot be less than previous reading");
    }

    @Test
    void shouldRejectNegativeConsumption() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .meterId(1L)
                .readingTimestamp(LocalDateTime.now())
                .readingType(com.powersphere.energy.enums.ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("2000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .readingSource(com.powersphere.energy.enums.ReadingSource.WEB)
                .build();

        assertThatThrownBy(() -> validator.validateNewReading(request))
                .isInstanceOf(InvalidReadingException.class)
                .hasMessageContaining("cannot be less than previous reading");
    }

    @Test
    void shouldRejectDuplicateReading() {
        LocalDateTime timestamp = LocalDateTime.now();

        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .meterId(1L)
                .readingTimestamp(timestamp)
                .readingType(com.powersphere.energy.enums.ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .readingSource(com.powersphere.energy.enums.ReadingSource.WEB)
                .build();

        when(repository.existsByMeterIdAndReadingTimestamp(eq(1L), any(LocalDateTime.class)))
                .thenReturn(true);

        assertThatThrownBy(() -> validator.validateNewReading(request))
                .isInstanceOf(DuplicateReadingException.class)
                .hasMessageContaining("Duplicate reading");
    }

    @Test
    void shouldAcceptValidReading() {
        LocalDateTime timestamp = LocalDateTime.now();

        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .meterId(1L)
                .readingTimestamp(timestamp)
                .readingType(com.powersphere.energy.enums.ReadingType.AUTOMATIC)
                .previousReading(new BigDecimal("1000.000"))
                .currentReading(new BigDecimal("1500.000"))
                .readingSource(com.powersphere.energy.enums.ReadingSource.WEB)
                .build();

        when(repository.existsByMeterIdAndReadingTimestamp(eq(1L), any(LocalDateTime.class)))
                .thenReturn(false);

        // Should not throw any exception
        validator.validateNewReading(request);
    }
}
