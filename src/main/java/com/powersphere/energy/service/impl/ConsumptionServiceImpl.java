package com.powersphere.energy.service.impl;

import com.powersphere.energy.dto.response.ConsumptionResponse;
import com.powersphere.energy.entity.EnergyReading;
import com.powersphere.energy.event.ConsumptionCalculatedEvent;
import com.powersphere.energy.mapper.EnergyReadingMapper;
import com.powersphere.energy.repository.EnergyReadingRepository;
import com.powersphere.energy.service.ConsumptionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumptionServiceImpl implements ConsumptionService {

    private final EnergyReadingRepository repository;
    private final EnergyReadingMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public ConsumptionResponse calculateConsumption(EnergyReading reading) {
        BigDecimal consumption = reading.getCurrentReading().subtract(reading.getPreviousReading());
        consumption = consumption.max(BigDecimal.ZERO);

        BigDecimal percentageChange = calculatePercentageChange(
                reading.getPreviousReading(), reading.getCurrentReading());

        ConsumptionResponse response = ConsumptionResponse.builder()
                .meterId(reading.getMeterId())
                .previousReading(reading.getPreviousReading())
                .currentReading(reading.getCurrentReading())
                .consumption(consumption)
                .percentageChange(percentageChange)
                .previousReadingTimestamp(null)
                .currentReadingTimestamp(reading.getReadingTimestamp())
                .unit("kWh")
                .build();

        eventPublisher.publishEvent(new ConsumptionCalculatedEvent(
                this, reading.getMeterId(), consumption, response));

        log.debug("Consumption calculated for meter {}: {}", reading.getMeterId(), consumption);
        return response;
    }

    @Override
    public ConsumptionResponse calculateConsumptionBetween(Long meterId, LocalDateTime startDate, LocalDateTime endDate) {
        List<EnergyReading> readings = repository
                .findByMeterIdAndReadingTimestampBetweenAndActiveTrueOrderByReadingTimestampAsc(
                        meterId, startDate, endDate);

        if (readings.isEmpty()) {
            throw new EntityNotFoundException(
                    "No readings found for meter ID " + meterId + " between " + startDate + " and " + endDate);
        }

        BigDecimal totalConsumption = BigDecimal.ZERO;
        for (EnergyReading reading : readings) {
            totalConsumption = totalConsumption.add(reading.getConsumption());
        }

        EnergyReading first = readings.getFirst();
        EnergyReading last = readings.get(readings.size() - 1);

        BigDecimal percentageChange = calculatePercentageChange(
                first.getPreviousReading(), last.getCurrentReading());

        ConsumptionResponse response = ConsumptionResponse.builder()
                .meterId(meterId)
                .previousReading(first.getPreviousReading())
                .currentReading(last.getCurrentReading())
                .consumption(totalConsumption)
                .percentageChange(percentageChange)
                .previousReadingTimestamp(first.getReadingTimestamp())
                .currentReadingTimestamp(last.getReadingTimestamp())
                .unit("kWh")
                .build();

        eventPublisher.publishEvent(new ConsumptionCalculatedEvent(
                this, meterId, totalConsumption, response));

        return response;
    }

    @Override
    public ConsumptionResponse getLatestConsumption(Long meterId) {
        EnergyReading latestReading = repository
                .findTopByMeterIdAndActiveTrueOrderByReadingTimestampDesc(meterId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No readings found for meter ID: " + meterId));
        return calculateConsumption(latestReading);
    }

    @Override
    public List<ConsumptionResponse> getConsumptionHistory(Long meterId) {
        List<EnergyReading> readings = repository
                .findByMeterIdAndActiveTrueOrderByReadingTimestampDesc(meterId);

        List<ConsumptionResponse> history = new ArrayList<>();
        for (EnergyReading reading : readings) {
            history.add(calculateConsumption(reading));
        }
        return history;
    }

    private BigDecimal calculatePercentageChange(BigDecimal previous, BigDecimal current) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
