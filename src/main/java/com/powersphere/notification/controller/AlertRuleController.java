package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.service.AlertRuleService;
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
public class AlertRuleController {

    private final AlertRuleService alertRuleService;

    @PostMapping
    public ResponseEntity<ApiResponse<AlertRuleResponse>> createAlertRule(
            @Valid @RequestBody CreateAlertRuleRequest request) {
        AlertRuleResponse response = alertRuleService.createAlertRule(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Alert rule created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertRuleResponse>> updateAlertRule(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAlertRuleRequest request) {
        AlertRuleResponse response = alertRuleService.updateAlertRule(id, request);
        return ResponseEntity.ok(ApiResponse.success("Alert rule updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAlertRule(@PathVariable Long id) {
        alertRuleService.deleteAlertRule(id);
        return ResponseEntity.ok(ApiResponse.success("Alert rule deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertRuleResponse>> getAlertRuleById(@PathVariable Long id) {
        AlertRuleResponse response = alertRuleService.getAlertRuleById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<AlertRuleResponse>>> getAllAlertRules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<AlertRuleResponse> response = alertRuleService.getAllAlertRules(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/event-type/{eventType}")
    public ResponseEntity<ApiResponse<List<AlertRuleResponse>>> getAlertRulesByEventType(
            @PathVariable String eventType) {
        List<AlertRuleResponse> response = alertRuleService.getAlertRulesByEventType(eventType);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AlertRuleResponse>> toggleAlertRuleStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        AlertRuleResponse response = alertRuleService.toggleAlertRuleStatus(id, active);
        return ResponseEntity.ok(ApiResponse.success("Alert rule status updated", response));
    }
}
