package com.powersphere.organization.controller;

import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.service.DepartmentService;
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
@RequestMapping("/api/v1/organizations/{organizationId}/departments")
@RequiredArgsConstructor
@Tag(name = "Department", description = "Department management APIs")
public class DepartmentController {

    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create a new department within an organization")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @PathVariable UUID organizationId,
            @Valid @RequestBody DepartmentRequest request) {
        log.info("REST request to create department in organization: {}", organizationId);
        var response = departmentService.createDepartment(organizationId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing department")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable UUID organizationId,
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequest request) {
        log.info("REST request to update department with id: {}", id);
        var response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a department")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("REST request to delete department with id: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
            @PathVariable UUID organizationId,
            @PathVariable UUID id) {
        log.info("REST request to get department by id: {}", id);
        var response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all departments for an organization")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentsByOrganization(
            @PathVariable UUID organizationId) {
        log.info("REST request to get departments for organization: {}", organizationId);
        var response = departmentService.getDepartmentsByOrganization(organizationId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
