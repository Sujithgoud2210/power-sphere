package com.powersphere.organization.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
            description = "Creates a new organization with the provided details including code, name, address, and contact information.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Organization created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Organization with same code already exists")
    })
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
            description = "Updates an existing organization's details including address, contact info, and status by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @Parameter(description = "Organization UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id,
            @Valid @RequestBody OrganizationRequest request) {
        log.debug("PUT /api/v1/organizations/{}", id);
        OrganizationResponse response = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(ApiResponse.success("Organization updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an organization",
            description = "Soft-deletes an organization by ID, marking it as inactive without removing data from the database.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Organization has active dependencies")
    })
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(
            @Parameter(description = "Organization UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id) {
        log.debug("DELETE /api/v1/organizations/{}", id);
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Organization deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get organization by ID",
            description = "Retrieves detailed information about an organization including address, contact info, and current status by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationById(
            @Parameter(description = "Organization UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id) {
        log.debug("GET /api/v1/organizations/{}", id);
        OrganizationResponse response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all organizations",
            description = "Retrieves a list of all active organizations in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organizations retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getAllOrganizations() {
        log.debug("GET /api/v1/organizations");
        List<OrganizationResponse> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(ApiResponse.success(organizations));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search organizations",
            description = "Search organizations by name. Returns matching organizations whose name contains the search term.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> searchOrganizations(
            @Parameter(description = "Organization name search term", example = "Acme Corp", required = true) @RequestParam String name) {
        log.debug("GET /api/v1/organizations/search?name={}", name);
        List<OrganizationResponse> organizations = organizationService.searchOrganizations(name);
        return ResponseEntity.ok(ApiResponse.success(organizations));
    }
}
