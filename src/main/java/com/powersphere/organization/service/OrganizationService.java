package com.powersphere.organization.service;

import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationResponse createOrganization(OrganizationRequest request);

    OrganizationResponse updateOrganization(UUID id, OrganizationRequest request);

    void deleteOrganization(UUID id);

    OrganizationResponse getOrganizationById(UUID id);

    List<OrganizationResponse> getAllOrganizations();

    List<OrganizationResponse> searchOrganizations(String searchTerm);

    List<OrganizationResponse> getOrganizationsByStatus(String status);
}
