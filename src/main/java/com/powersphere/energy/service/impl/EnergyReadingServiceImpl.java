package com.powersphere.energy.service.impl;

import com.powersphere.energy.dto.request.EnergyReadingRequest;
import com.powersphere.energy.dto.request.EnergySearchRequest;
import com.powersphere.energy.dto.response.EnergyReadingResponse;
import com.powersphere.energy.entity.EnergyReading;
import com.powersphere.energy.event.EnergyReadingCreatedEvent;
import com.powersphere.energy.mapper.EnergyReadingMapper;
import com.powersphere.energy.repository.EnergyReadingRepository;
import com.powersphere.energy.service.EnergyReadingService;
import com.powersphere.energy.validation.EnergyReadingValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnergyReadingServiceImpl implements EnergyReadingService {

    private final EnergyReadingRepository repository;
    private final EnergyReadingMapper mapper;
    private final EnergyReadingValidator validator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public EnergyReadingResponse createReading(EnergyReadingRequest request) {
        log.debug("Creating new energy reading for meter ID: {}", request.getMeterId());

        // Validate the request
        validator.validateNewReading(request);

        // Map request to entity
        EnergyReading reading = mapper.toEntity(request);

        // Calculate consumption
        BigDecimal consumption = request.getCurrentReading().subtract(request.getPreviousReading());
        reading.setConsumption(consumption);

        // Save the reading
        EnergyReading savedReading = repository.save(reading);

        // Publish event
        eventPublisher.publishEvent(new EnergyReadingCreatedEvent(this, savedReading));

        log.info("Energy reading created successfully with ID: {}", savedReading.getId());
        return mapper.toResponse(savedReading);
    }

    @Override
    @Transactional
    public EnergyReadingResponse updateReading(Long id, EnergyReadingRequest request) {
        log.debug("Updating energy reading with ID: {}", id);

        // Find existing reading
        EnergyReading existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Energy reading not found with ID: " + id));

        // Validate the update
        validator.validateUpdateReading(id, request);

        // Update entity fields
        mapper.updateEntity(existing, request);

        // Recalculate consumption
        BigDecimal consumption = request.getCurrentReading().subtract(request.getPreviousReading());
        existing.setConsumption(consumption);

        // Save the updated reading
        EnergyReading updatedReading = repository.save(existing);

        log.info("Energy reading updated successfully with ID: {}", updatedReading.getId());
        return mapper.toResponse(updatedReading);
    }

    @Override
    @Transactional
    public void deleteReading(Long id) {
        log.debug("Soft-deleting energy reading with ID: {}", id);

        EnergyReading reading = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Energy reading not found with ID: " + id));

        reading.setActive(false);
        repository.save(reading);

        log.info("Energy reading soft-deleted with ID: {}", id);
    }

    @Override
    public EnergyReadingResponse getReading(Long id) {
        log.debug("Fetching energy reading with ID: {}", id);

        EnergyReading reading = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Energy reading not found with ID: " + id));

        return mapper.toResponse(reading);
    }

    @Override
    public Page<EnergyReadingResponse> listReadings(int page, int size) {
        log.debug("Listing energy readings - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "readingTimestamp"));

        return repository.findByActiveTrue(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<EnergyReadingResponse> searchReadings(EnergySearchRequest searchRequest) {
        log.debug("Searching energy readings with filters: {}", searchRequest);

        Specification<EnergyReading> spec = buildSearchSpecification(searchRequest);

        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                Sort.by(searchRequest.getSortDirection(), searchRequest.getSortBy())
        );

        return repository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Override
    public EnergyReadingResponse getLatestReading(Long meterId) {
        log.debug("Fetching latest energy reading for meter ID: {}", meterId);

        EnergyReading reading = repository
                .findTopByMeterIdAndActiveTrueOrderByReadingTimestampDesc(meterId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No readings found for meter ID: " + meterId));

        return mapper.toResponse(reading);
    }

    @Override
    public List<EnergyReadingResponse> getReadingHistory(Long meterId) {
        log.debug("Fetching reading history for meter ID: {}", meterId);

        List<EnergyReading> readings = repository
                .findByMeterIdAndActiveTrueOrderByReadingTimestampDesc(meterId);

        if (readings.isEmpty()) {
            throw new EntityNotFoundException(
                    "No readings found for meter ID: " + meterId);
        }

        return readings.stream()
                .map(mapper::toResponse)
                .toList();
    }

    /**
     * Builds a JPA Specification for dynamic search/filter queries.
     */
    private Specification<EnergyReading> buildSearchSpecification(EnergySearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter active readings
            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            // Filter by meter ID
            if (request.getMeterId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("meterId"), request.getMeterId()));
            }

            // Filter by reading type
            if (request.getReadingType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("readingType"), request.getReadingType()));
            }

            // Filter by quality status
            if (request.getQualityStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("qualityStatus"), request.getQualityStatus()));
            }

            // Filter by date range
            if (request.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("readingTimestamp"), request.getStartDate()));
            }
            if (request.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("readingTimestamp"), request.getEndDate()));
            }

            // Search keyword in remarks
            if (request.getSearchKeyword() != null && !request.getSearchKeyword().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("remarks")),
                        "%" + request.getSearchKeyword().toLowerCase() + "%"));
            }

            // Filter by meter number (via meterId lookup — meter number resolution would require Meter module)
            // Note: meterNumber filtering requires cross-module reference to Meter entity.
            // When the Meter module is available, add a join/lookup here to resolve meterNumber to meterId.
            if (request.getMeterNumber() != null && !request.getMeterNumber().isBlank()) {
                // Placeholder: log that meter number filtering was requested
                // predicates.add(criteriaBuilder.equal(root.get("meter").get("meterNumber"), request.getMeterNumber()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
