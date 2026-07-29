package com.powersphere.organization.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = ApplicationConstants.API_BASE_PATH + "/organizations",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Organizations", description = "Organization management endpoints")
public class OrganizationController {

    private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new organization",
            description = "Creates a new organization with the provided details")
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @Valid @RequestBody OrganizationRequest request) {
        log.debug("POST /api/v1/organizations - code: {}", request.getOrganizationCode());
        OrganizationResponse response = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Organization created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an organization",
            description = "Updates an existing organization by ID")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationRequest request) {
        log.debug("PUT /api/v1/organizations/{}", id);
        OrganizationResponse response = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(ApiResponse.success("Organization updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an organization",
            description = "Soft-deletes an organization by ID")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable UUID id) {
        log.debug("DELETE /api/v1/organizations/{}", id);
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Organization deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get organization by ID",
            description = "Retrieves organization details by ID")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationById(@PathVariable UUID id) {
        log.debug("GET /api/v1/organizations/{}", id);
        OrganizationResponse response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all organizations",
            description = "Retrieves all active organizations")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getAllOrganizations() {
        log.debug("GET /api/v1/organizations");
        List<OrganizationResponse> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(ApiResponse.success(organizations));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search organizations",
            description = "Search organizations by name")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> searchOrganizations(
            @RequestParam String name) {
        log.debug("GET /api/v1/organizations/search?name={}", name);
        List<OrganizationResponse> organizations = organizationService.searchOrganizations(name);
        return ResponseEntity.ok(ApiResponse.success(organizations));
    }
}
