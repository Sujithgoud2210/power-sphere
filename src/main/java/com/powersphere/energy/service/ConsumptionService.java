package com.powersphere.energy.service;

import com.powersphere.energy.dto.response.ConsumptionResponse;
import com.powersphere.energy.entity.EnergyReading;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for consumption-related calculations and operations.
 */
public interface ConsumptionService {

    /**
     * Calculates consumption from a single reading by comparing with the previous reading.
     *
     * @param reading the energy reading to calculate consumption for
     * @return the calculated consumption value
     */
    ConsumptionResponse calculateConsumption(EnergyReading reading);

    /**
     * Calculates consumption between two specific dates for a meter.
     *
     * @param meterId   the meter ID
     * @param startDate the start date (inclusive)
     * @param endDate   the end date (inclusive)
     * @return the calculated consumption response
     */
    ConsumptionResponse calculateConsumptionBetween(Long meterId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Gets the latest consumption for a meter.
     *
     * @param meterId the meter ID
     * @return the latest consumption response
     */
    ConsumptionResponse getLatestConsumption(Long meterId);

    /**
     * Gets consumption history for a meter.
     *
     * @param meterId the meter ID
     * @return list of consumption responses
     */
    List<ConsumptionResponse> getConsumptionHistory(Long meterId);
}
