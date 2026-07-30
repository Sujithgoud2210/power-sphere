package com.powersphere.meter.service.impl;

import com.powersphere.authentication.entity.User;
import com.powersphere.authentication.exception.UserNotFoundException;
import com.powersphere.authentication.repository.UserRepository;
import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.meter.dto.request.*;
import com.powersphere.meter.dto.response.MeterResponse;
import com.powersphere.meter.entity.SmartMeter;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.event.MeterAssignedEvent;
import com.powersphere.meter.event.MeterRegisteredEvent;
import com.powersphere.meter.event.MeterTransferredEvent;
import com.powersphere.meter.exception.InvalidMeterStateException;
import com.powersphere.meter.exception.MeterAssignmentException;
import com.powersphere.meter.exception.MeterNotFoundException;
import com.powersphere.meter.mapper.MeterMapper;
import com.powersphere.meter.repository.SmartMeterRepository;
import com.powersphere.meter.service.MeterService;
import com.powersphere.meter.util.MeterUtil;
import com.powersphere.meter.validation.MeterValidator;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private static final Logger log = LoggerFactory.getLogger(MeterServiceImpl.class);

    private final SmartMeterRepository smartMeterRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final MeterMapper meterMapper;
    private final MeterValidator meterValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public MeterResponse registerMeter(MeterRegistrationRequest request) {
        log.debug("Registering new meter with number: {}", request.getMeterNumber());

        // Validate uniqueness
        meterValidator.validateRegistration(request);

        // Map request to entity
        SmartMeter meter = meterMapper.toEntity(request);

        // Set organization if provided
        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException(
                            "Organization not found with id: " + request.getOrganizationId()));
            meter.setOrganization(organization);
        }

        // Set department if provided
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(
                            "Department not found with id: " + request.getDepartmentId()));
            meter.setDepartment(department);
        }

        // Generate QR code and barcode
        meter.setStatus(MeterStatus.INACTIVE);
        meter = smartMeterRepository.save(meter);

        // Generate barcode and QR code with the generated ID
        meter.setQrCode(MeterUtil.generateQrCode(meter.getMeterNumber(), meter.getSerialNumber(), meter.getId()));
        meter.setBarcode(MeterUtil.generateBarcode(meter.getMeterNumber(), meter.getId()));
        meter = smartMeterRepository.save(meter);

        // Publish event
        eventPublisher.publishEvent(new MeterRegisteredEvent(this, meter));

        log.info("Meter registered successfully with id: {} and number: {}", meter.getId(), meter.getMeterNumber());
        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public MeterResponse updateMeter(UUID id, MeterUpdateRequest request) {
        log.debug("Updating meter with id: {}", id);

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        meterMapper.updateEntity(meter, request);
        meter = smartMeterRepository.save(meter);

        log.info("Meter updated successfully with id: {}", id);
        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public void deleteMeter(UUID id) {
        log.debug("Deleting (soft) meter with id: {}", id);

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        meter.setIsActive(false);
        meter.setStatus(MeterStatus.REMOVED);
        smartMeterRepository.save(meter);

        log.info("Meter soft-deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public MeterResponse getMeterById(UUID id) {
        log.debug("Fetching meter by id: {}", id);

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeterResponse> getAllMeters(int page, int size, String sortBy, String sortDirection) {
        log.debug("Fetching all meters - page: {}, size: {}", page, size);

        Sort sort = Sort.by(getSortDirection(sortDirection), sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        return smartMeterRepository.findByIsActiveTrue(pageable)
                .map(meterMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MeterResponse getMeterByMeterNumber(String meterNumber) {
        log.debug("Fetching meter by number: {}", meterNumber);

        SmartMeter meter = smartMeterRepository.findByMeterNumber(meterNumber)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with number: " + meterNumber));

        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional(readOnly = true)
    public MeterResponse getMeterBySerialNumber(String serialNumber) {
        log.debug("Fetching meter by serial number: {}", serialNumber);

        SmartMeter meter = smartMeterRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with serial number: " + serialNumber));

        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeterResponse> searchMeters(String searchTerm, int page, int size,
                                             String sortBy, String sortDirection) {
        log.debug("Searching meters with term: {} - page: {}, size: {}", searchTerm, page, size);

        Sort sort = Sort.by(getSortDirection(sortDirection), sortBy != null ? sortBy : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        return smartMeterRepository.searchMeters(searchTerm, pageable)
                .map(meterMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MeterResponse> filterMeters(MeterSearchRequest searchRequest) {
        log.debug("Filtering meters with filters");

        String sortBy = searchRequest.getSortBy() != null ? searchRequest.getSortBy() : "createdAt";
        String sortDirection = searchRequest.getSortDirection() != null ? searchRequest.getSortDirection() : "DESC";
        int page = Math.max(searchRequest.getPage(), 0);
        int size = searchRequest.getSize() > 0 ? Math.min(searchRequest.getSize(), ApplicationConstants.MAX_PAGE_SIZE)
                : ApplicationConstants.DEFAULT_PAGE_SIZE;

        Sort sort = Sort.by(getSortDirection(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return smartMeterRepository.filterMeters(
                        searchRequest.getSearchTerm(),
                        searchRequest.getStatus(),
                        searchRequest.getMeterType(),
                        searchRequest.getPhaseType(),
                        searchRequest.getConnectionType(),
                        searchRequest.getOrganizationId(),
                        searchRequest.getAssignedUserId(),
                        searchRequest.getCity(),
                        searchRequest.getState(),
                        searchRequest.getCountry(),
                        searchRequest.getInstallationDateFrom(),
                        searchRequest.getInstallationDateTo(),
                        pageable)
                .map(meterMapper::toResponse);
    }

    @Override
    @Transactional
    public MeterResponse activateMeter(UUID id) {
        log.debug("Activating meter with id: {}", id);

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        if (meter.getStatus() == MeterStatus.ACTIVE) {
            throw new InvalidMeterStateException("Meter is already active");
        }

        if (meter.getStatus() == MeterStatus.REMOVED) {
            throw new InvalidMeterStateException("Cannot activate a removed meter");
        }

        meter.setStatus(MeterStatus.ACTIVE);
        meter.setActivationDate(LocalDateTime.now());
        meter = smartMeterRepository.save(meter);

        log.info("Meter activated successfully with id: {}", id);
        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public MeterResponse deactivateMeter(UUID id) {
        log.debug("Deactivating meter with id: {}", id);

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        if (meter.getStatus() == MeterStatus.INACTIVE) {
            throw new InvalidMeterStateException("Meter is already inactive");
        }

        if (meter.getStatus() == MeterStatus.REMOVED) {
            throw new InvalidMeterStateException("Cannot deactivate a removed meter");
        }

        meter.setStatus(MeterStatus.INACTIVE);
        meter = smartMeterRepository.save(meter);

        log.info("Meter deactivated successfully with id: {}", id);
        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public MeterResponse assignMeter(UUID id, AssignMeterRequest request) {
        log.debug("Assigning meter {} to user {}", id, request.getUserId());

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        if (meter.getStatus() != MeterStatus.ACTIVE) {
            throw new InvalidMeterStateException(
                    "Cannot assign meter. Current status: " + meter.getStatus() + ". Meter must be ACTIVE.");
        }

        if (meter.getAssignedUser() != null) {
            throw new MeterAssignmentException(
                    "Meter is already assigned to a user. Use transfer endpoint instead.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.getUserId()));

        meter.setAssignedUser(user);
        meter = smartMeterRepository.save(meter);

        // Publish event
        eventPublisher.publishEvent(new MeterAssignedEvent(this, meter, request.getUserId()));

        log.info("Meter {} assigned to user {} successfully", id, request.getUserId());
        return meterMapper.toResponse(meter);
    }

    @Override
    @Transactional
    public MeterResponse transferMeter(UUID id, TransferMeterRequest request) {
        log.debug("Transferring meter {} from user {} to user {}", id, request.getCurrentUserId(), request.getNewUserId());

        SmartMeter meter = smartMeterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException("Meter not found with id: " + id));

        if (meter.getAssignedUser() == null) {
            throw new MeterAssignmentException("Meter is not assigned to any user. Use assign endpoint instead.");
        }

        if (!meter.getAssignedUser().getId().equals(request.getCurrentUserId())) {
            throw new MeterAssignmentException(
                    "Meter is not assigned to the specified current user");
        }

        if (request.getCurrentUserId().equals(request.getNewUserId())) {
            throw new MeterAssignmentException(
                    "Current user and new user cannot be the same");
        }

        User newUser = userRepository.findById(request.getNewUserId())
                .orElseThrow(() -> new UserNotFoundException("New user not found with id: " + request.getNewUserId()));

        UUID previousUserId = meter.getAssignedUser().getId();
        meter.setAssignedUser(newUser);
        meter = smartMeterRepository.save(meter);

        // Publish event
        eventPublisher.publishEvent(new MeterTransferredEvent(this, meter, previousUserId, request.getNewUserId()));

        log.info("Meter {} transferred from user {} to user {} successfully",
                id, request.getCurrentUserId(), request.getNewUserId());
        return meterMapper.toResponse(meter);
    }

    private Sort.Direction getSortDirection(String sortDirection) {
        if (sortDirection == null) {
            return Sort.Direction.DESC;
        }
        try {
            return Sort.Direction.fromString(sortDirection.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Sort.Direction.DESC;
        }
    }
}
