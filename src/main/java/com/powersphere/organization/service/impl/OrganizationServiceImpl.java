package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.exception.DuplicateOrganizationException;
import com.powersphere.organization.exception.OrganizationNotFoundException;
import com.powersphere.organization.mapper.OrganizationMapper;
import com.powersphere.organization.repository.OrganizationRepository;
import com.powersphere.organization.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Override
    @Transactional
    public OrganizationResponse createOrganization(OrganizationRequest request) {
        log.debug("Creating organization with code: {}", request.getOrganizationCode());

        if (organizationRepository.existsByOrganizationCode(request.getOrganizationCode())) {
            throw new DuplicateOrganizationException(
                    "Organization with code '" + request.getOrganizationCode() + "' already exists");
        }

        var organization = organizationMapper.toEntity(request);
        organization = organizationRepository.save(organization);
        log.info("Organization created successfully with id: {}", organization.getId());
        return organizationMapper.toResponse(organization);
    }

    @Override
    @Transactional
    public OrganizationResponse updateOrganization(UUID id, OrganizationRequest request) {
        log.debug("Updating organization with id: {}", id);

        var organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + id));

        organizationMapper.updateEntity(organization, request);
        organization = organizationRepository.save(organization);
        log.info("Organization updated successfully with id: {}", id);
        return organizationMapper.toResponse(organization);
    }

    @Override
    @Transactional
    public void deleteOrganization(UUID id) {
        log.debug("Deleting organization with id: {}", id);

        var organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + id));

        organization.setIsActive(false);
        organizationRepository.save(organization);
        log.info("Organization soft-deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(UUID id) {
        log.debug("Fetching organization by id: {}", id);

        var organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found with id: " + id));

        return organizationMapper.toResponse(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllOrganizations() {
        log.debug("Fetching all active organizations");

        return organizationRepository.findByIsActiveTrue()
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> searchOrganizations(String searchTerm) {
        log.debug("Searching organizations with term: {}", searchTerm);

        return organizationRepository.findByOrganizationNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getOrganizationsByStatus(String status) {
        log.debug("Fetching organizations by status: {}", status);

        return organizationRepository.findByStatus(status)
                .stream()
                .map(organizationMapper::toResponse)
                .toList();
    }
}
