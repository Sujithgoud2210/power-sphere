package com.powersphere.energy.service;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.dto.request.EnergySearchRequest;
import com.powersphere.energy.dto.response.EnergyReadingResponse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for energy reading management operations.
 */
public interface EnergyReadingService {

    /**
     * Creates a new energy reading after validation.
     *
     * @param request the energy reading request
     * @return the created energy reading response
     */
    EnergyReadingResponse createReading(EnergyReadingRequest request);

    /**
     * Updates an existing energy reading.
     *
     * @param id      the reading ID
     * @param request the updated energy reading request
     * @return the updated energy reading response
     */
    EnergyReadingResponse updateReading(Long id, EnergyReadingRequest request);

    /**
     * Soft-deletes an energy reading.
     *
     * @param id the reading ID
     */
    void deleteReading(Long id);

    /**
     * Retrieves an energy reading by ID.
     *
     * @param id the reading ID
     * @return the energy reading response
     */
    EnergyReadingResponse getReading(Long id);

    /**
     * Lists all active energy readings with pagination.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of energy reading responses
     */
    Page<EnergyReadingResponse> listReadings(int page, int size);

    /**
     * Searches and filters energy readings.
     *
     * @param searchRequest the search/filter request
     * @return paginated list of matching energy reading responses
     */
    Page<EnergyReadingResponse> searchReadings(EnergySearchRequest searchRequest);

    /**
     * Gets the latest reading for a specific meter.
     *
     * @param meterId the meter ID
     * @return the latest energy reading response
     */
    EnergyReadingResponse getLatestReading(Long meterId);

    /**
     * Gets reading history for a specific meter.
     *
     * @param meterId the meter ID
     * @return list of energy reading responses ordered by timestamp descending
     */
    List<EnergyReadingResponse> getReadingHistory(Long meterId);
}
