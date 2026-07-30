package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.service.AlertRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alert-rules")
@RequiredArgsConstructor
@Tag(name = "Alert Rules", description = "Configurable alert rule definitions for automated notifications")
public class AlertRuleController {

    private final AlertRuleService alertRuleService;

    @PostMapping
    @Operation(summary = "Create a new alert rule",
            description = "Creates a new alert rule that defines conditions for triggering automated notifications. Rules can be based on consumption thresholds, meter status changes, billing events, and more.",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Alert rule created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed")
    })
    public ResponseEntity<ApiResponse<AlertRuleResponse>> createAlertRule(
            @Valid @RequestBody CreateAlertRuleRequest request) {
        AlertRuleResponse response = alertRuleService.createAlertRule(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Alert rule created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an alert rule",
            description = "Updates the conditions, actions, or status of an existing alert rule identified by its ID.",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Alert rule updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Alert rule not found")
    })
    public ResponseEntity<ApiResponse<AlertRuleResponse>> updateAlertRule(
            @Parameter(description = "Alert rule ID", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateAlertRuleRequest request) {
        AlertRuleResponse response = alertRuleService.updateAlertRule(id, request);
        return ResponseEntity.ok(ApiResponse.success("Alert rule updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an alert rule",
            description = "Deletes an alert rule by its ID. This is a soft delete that deactivates the rule without removing it from the database.",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Alert rule deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Alert rule not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteAlertRule(
            @Parameter(description = "Alert rule ID", example = "1", required = true) @PathVariable Long id) {
        alertRuleService.deleteAlertRule(id);
        return ResponseEntity.ok(ApiResponse.success("Alert rule deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get alert rule by ID",
            description = "Retrieves detailed information about a specific alert rule including its conditions, actions, and current status.",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Alert rule retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Alert rule not found")
    })
    public ResponseEntity<ApiResponse<AlertRuleResponse>> getAlertRuleById(
            @Parameter(description = "Alert rule ID", example = "1", required = true) @PathVariable Long id) {
        AlertRuleResponse response = alertRuleService.getAlertRuleById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all alert rules with pagination",
            description = "Retrieves a paginated list of all alert rules with sorting support.",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Alert rules retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PagedResponse<AlertRuleResponse>>> getAllAlertRules(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (createdAt, name, eventType)", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<AlertRuleResponse> response = alertRuleService.getAllAlertRules(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/event-type/{eventType}")
    @Operation(summary = "Get alert rules by event type",
            description = "Retrieves all alert rules associated with a specific event type (e.g., HIGH_CONSUMPTION, METER_FAILURE, BILL_GENERATED).",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Alert rules retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<AlertRuleResponse>>> getAlertRulesByEventType(
            @Parameter(description = "Event type to filter by", example = "HIGH_CONSUMPTION", required = true) @PathVariable String eventType) {
        List<AlertRuleResponse> response = alertRuleService.getAlertRulesByEventType(eventType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Toggle alert rule status",
            description = "Enables or disables an alert rule. Disabled rules will not trigger notifications even if their conditions are met.",
            tags = {"Alert Rules"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Alert rule status updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Alert rule not found")
    })
    public ResponseEntity<ApiResponse<AlertRuleResponse>> toggleAlertRuleStatus(
            @Parameter(description = "Alert rule ID", example = "1", required = true) @PathVariable Long id,
            @Parameter(description = "New active status (true = enabled, false = disabled)", example = "true", required = true) @RequestParam boolean active) {
        AlertRuleResponse response = alertRuleService.toggleAlertRuleStatus(id, active);
        return ResponseEntity.ok(ApiResponse.success("Alert rule status updated", response));
    }
}
