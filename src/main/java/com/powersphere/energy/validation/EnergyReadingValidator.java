package com.powersphere.energy.validation;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.exception.DuplicateReadingException;
import com.powersphere.energy.exception.InvalidReadingException;
import com.powersphere.energy.repository.EnergyReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnergyReadingValidator {

    private final EnergyReadingRepository repository;

    /**
     * Validates a new energy reading request.
     */
    public void validateNewReading(EnergyReadingRequest request) {
        // Validate chronological order
        validateReadingOrder(request);

        // Validate consumption is non-negative
        validateConsumption(request);

        // Validate duplicate reading
        validateDuplicate(request.getMeterId(), request.getReadingTimestamp(), null);
    }

    /**
     * Validates an update to an existing energy reading request.
     */
    public void validateUpdateReading(Long readingId, EnergyReadingRequest request) {
        // Validate chronological order
        validateReadingOrder(request);

        // Validate consumption is non-negative
        validateConsumption(request);

        // Validate duplicate reading excluding current ID
        validateDuplicate(request.getMeterId(), request.getReadingTimestamp(), readingId);
    }

    /**
     * Validates that the current reading is not less than the previous reading.
     */
    private void validateReadingOrder(EnergyReadingRequest request) {
        if (request.getCurrentReading().compareTo(request.getPreviousReading()) < 0) {
            log.warn("Invalid reading order: current reading {} is less than previous reading {}",
                    request.getCurrentReading(), request.getPreviousReading());
            throw new InvalidReadingException(
                    "Current reading (%s) cannot be less than previous reading (%s)",
                    request.getCurrentReading(), request.getPreviousReading());
        }
    }

    /**
     * Validates that consumption (current - previous) is non-negative.
     */
    private void validateConsumption(EnergyReadingRequest request) {
        BigDecimal calculatedConsumption = request.getCurrentReading().subtract(request.getPreviousReading());
        if (calculatedConsumption.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Negative consumption calculated: {}", calculatedConsumption);
            throw new InvalidReadingException(
                    "Consumption cannot be negative. Calculated: %s", calculatedConsumption);
        }
    }

    /**
     * Validates that no duplicate reading exists for the same meter and timestamp.
     */
    private void validateDuplicate(Long meterId, LocalDateTime timestamp, Long excludeId) {
        boolean exists;
        if (excludeId != null) {
            exists = repository.existsByMeterIdAndReadingTimestampExcludingId(meterId, timestamp, excludeId);
        } else {
            exists = repository.existsByMeterIdAndReadingTimestamp(meterId, timestamp);
        }

        if (exists) {
            log.warn("Duplicate reading detected for meter ID {} at timestamp {}", meterId, timestamp);
            throw new DuplicateReadingException(meterId, timestamp.toString());
        }
    }
}
