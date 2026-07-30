package com.powersphere.organization.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.DepartmentRequest;
import com.powersphere.organization.dto.response.DepartmentResponse;
import com.powersphere.organization.service.DepartmentService;
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
            description = "Creates a new department within an organization with the provided name, code, and description.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Department created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Department with same code already exists")
    })
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
            description = "Updates an existing department's details including name, code, description, and manager by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @Parameter(description = "Department UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id,
            @Valid @RequestBody DepartmentRequest request) {
        log.debug("PUT /api/v1/departments/{}", id);
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a department",
            description = "Soft-deletes a department by ID, marking it as inactive without removing data from the database.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Department has active teams or users")
    })
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @Parameter(description = "Department UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id) {
        log.debug("DELETE /api/v1/departments/{}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Department deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get department by ID",
            description = "Retrieves detailed information about a department including name, code, manager, and associated organization.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(
            @Parameter(description = "Department UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id) {
        log.debug("GET /api/v1/departments/{}", id);
        DepartmentResponse response = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all departments",
            description = "Retrieves a list of all active departments in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        log.debug("GET /api/v1/departments");
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search departments",
            description = "Search departments by name. Returns matching departments whose name contains the search term.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> searchDepartments(
            @Parameter(description = "Department name search term", example = "Engineering", required = true) @RequestParam String name) {
        log.debug("GET /api/v1/departments/search?name={}", name);
        List<DepartmentResponse> departments = departmentService.searchDepartments(name);
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/by-organization/{organizationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get departments by organization",
            description = "Retrieves all departments belonging to a specific organization.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentsByOrganization(
            @Parameter(description = "Organization UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID organizationId) {
        log.debug("GET /api/v1/departments/by-organization/{}", organizationId);
        List<DepartmentResponse> departments = departmentService.getDepartmentsByOrganization(organizationId);
        return ResponseEntity.ok(ApiResponse.success(departments));
    }
}
