package com.powersphere.organization.controller;

import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.OrganizationRequest;
import com.powersphere.organization.dto.response.OrganizationResponse;
import com.powersphere.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@Tag(name = "Organization", description = "Organization management APIs")
public class OrganizationController {

    private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);

    private final OrganizationService organizationService;

    @PostMapping
    @Operation(summary = "Create a new organization")
    public ResponseEntity<ApiResponse<OrganizationResponse>> createOrganization(
            @Valid @RequestBody OrganizationRequest request) {
        log.info("REST request to create organization");
        var response = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Organization created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organization")
    public ResponseEntity<ApiResponse<OrganizationResponse>> updateOrganization(
            @PathVariable UUID id,
            @Valid @RequestBody OrganizationRequest request) {
        log.info("REST request to update organization with id: {}", id);
        var response = organizationService.updateOrganization(id, request);
        return ResponseEntity.ok(ApiResponse.success("Organization updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete an organization")
    public ResponseEntity<ApiResponse<Void>> deleteOrganization(@PathVariable UUID id) {
        log.info("REST request to delete organization with id: {}", id);
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok(ApiResponse.success("Organization deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get organization by ID")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getOrganizationById(@PathVariable UUID id) {
        log.info("REST request to get organization by id: {}", id);
        var response = organizationService.getOrganizationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all organizations")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getAllOrganizations() {
        log.info("REST request to get all organizations");
        var response = organizationService.getAllOrganizations();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search organizations by name")
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> searchOrganizations(
            @RequestParam String q) {
        log.info("REST request to search organizations with query: {}", q);
        var response = organizationService.searchOrganizations(q);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
