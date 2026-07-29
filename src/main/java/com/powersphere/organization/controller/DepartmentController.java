package com.powersphere.organization.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(path = ApplicationConstants.API_BASE_PATH + "/departments",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Departments", description = "Department management endpoints")
public class DepartmentController {

    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new department",
            description = "Creates a new department within an organization")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody DepartmentRequest request) {
        log.debug("POST /api/v1/departments - code: {}", request.getCode());
        DepartmentResponse response = departmentService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a department",
            description = "Updates an existing department by ID")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequest request) {
        log.debug("PUT /api/v1/departments/{}", id);
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a department",
            description = "Soft-deletes a department by ID")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        log.debug("DELETE /api/v1/departments/{}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Department deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get department by ID",
            description = "Retrieves department details by ID")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable UUID id) {
        log.debug("GET /api/v1/departments/{}", id);
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all departments",
            description = "Retrieves all active departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        log.debug("GET /api/v1/departments");
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search departments",
            description = "Search departments by name")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> searchDepartments(
            @RequestParam String name) {
        log.debug("GET /api/v1/departments/search?name={}", name);
        List<DepartmentResponse> departments = departmentService.searchDepartments(name);
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/by-organization/{organizationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get departments by organization",
            description = "Retrieves all departments belonging to an organization")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentsByOrganization(
            @PathVariable UUID organizationId) {
        log.debug("GET /api/v1/departments/by-organization/{}", organizationId);
        List<DepartmentResponse> departments = departmentService.getDepartmentsByOrganization(organizationId);
        return ResponseEntity.ok(ApiResponse.success(departments));
    }
}
