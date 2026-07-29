package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.exception.DuplicateOrganizationException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.mapper.OrganizationMapper;
import com.powersphere.organization.repository.OrganizationRepository;
import com.powersphere.organization.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationResponse createOrganization(OrganizationRequest request) {
        log.info("Creating organization with code: {}", request.getOrganizationCode());

        if (organizationRepository.existsByOrganizationCode(request.getOrganizationCode())) {
            throw new DuplicateOrganizationException(
                    "Organization with code '" + request.getOrganizationCode() + "' already exists");
        }

        Organization organization = organizationMapper.toEntity(request);

        if (organization.getStatus() == null) {
            organization.setStatus("ACTIVE");
        }

        Organization savedOrganization = organizationRepository.save(organization);
        log.info("Organization created with id: {}", savedOrganization.getId());

        return organizationMapper.toResponse(savedOrganization);
    }

    @Override
    public OrganizationResponse updateOrganization(UUID id, OrganizationRequest request) {
        log.info("Updating organization with id: {}", id);

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found with id: " + id));

        if (!organization.getOrganizationCode().equals(request.getOrganizationCode())
                && organizationRepository.existsByOrganizationCode(request.getOrganizationCode())) {
            throw new DuplicateOrganizationException(
                    "Organization with code '" + request.getOrganizationCode() + "' already exists");
        }

        organizationMapper.updateEntity(organization, request);
        Organization updatedOrganization = organizationRepository.save(organization);
        log.info("Organization updated with id: {}", updatedOrganization.getId());

        return organizationMapper.toResponse(updatedOrganization);
    }

    @Override
    @Transactional
    public void deleteOrganization(UUID id) {
        log.info("Deleting organization with id: {}", id);

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found with id: " + id));

        organization.setIsActive(false);
        organizationRepository.save(organization);
        log.info("Organization soft-deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(UUID id) {
        log.debug("Fetching organization by id: {}", id);

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found with id: " + id));

        return organizationMapper.toResponse(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllOrganizations() {
        log.debug("Fetching all organizations");

        return organizationRepository.findByIsActiveTrue().stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> searchOrganizations(String name) {
        log.debug("Searching organizations by name: {}", name);

        return organizationRepository.findByOrganizationNameContainingIgnoreCase(name).stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getOrganizationsByStatus(String status) {
        log.debug("Fetching organizations by status: {}", status);

        return organizationRepository.findByStatus(status).stream()
                .map(organizationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
