package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.service.NotificationPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification-preferences")
@RequiredArgsConstructor
@Tag(name = "Notification Preferences", description = "User notification channel and type preferences management")
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    @PostMapping
    @Operation(summary = "Create notification preference",
            description = "Creates a new notification preference for a user, defining their preferred notification channels (EMAIL, SMS, PUSH, IN_APP) and notification types.",
            tags = {"Notification Preferences"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification preference created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed")
    })
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> createPreference(
            @Valid @RequestBody NotificationPreferenceRequest request) {
        NotificationPreferenceResponse response = preferenceService.createPreference(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification preference created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update notification preference",
            description = "Updates an existing notification preference, such as changing preferred channels or toggling notification types.",
            tags = {"Notification Preferences"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification preference updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification preference not found")
    })
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> updatePreference(
            @Parameter(description = "Preference ID", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody NotificationPreferenceRequest request) {
        NotificationPreferenceResponse response = preferenceService.updatePreference(id, request);
        return ResponseEntity.ok(ApiResponse.success("Notification preference updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification preference",
            description = "Deletes a notification preference by its ID. The user will revert to default notification settings.",
            tags = {"Notification Preferences"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification preference deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification preference not found")
    })
    public ResponseEntity<ApiResponse<Void>> deletePreference(
            @Parameter(description = "Preference ID", example = "1", required = true) @PathVariable Long id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.ok(ApiResponse.success("Notification preference deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification preference by ID",
            description = "Retrieves detailed information about a specific notification preference including channels, types, and enabled status.",
            tags = {"Notification Preferences"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification preference retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification preference not found")
    })
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> getPreferenceById(
            @Parameter(description = "Preference ID", example = "1", required = true) @PathVariable Long id) {
        NotificationPreferenceResponse response = preferenceService.getPreferenceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get preferences by user ID",
            description = "Retrieves all notification preferences configured for a specific user.",
            tags = {"Notification Preferences"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Preferences retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<NotificationPreferenceResponse>>> getPreferencesByUserId(
            @Parameter(description = "User ID", example = "1", required = true) @PathVariable Long userId) {
        List<NotificationPreferenceResponse> response = preferenceService.getPreferencesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle notification preference",
            description = "Enables or disables a specific notification preference without modifying its other settings.",
            tags = {"Notification Preferences"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification preference toggled"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification preference not found")
    })
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> togglePreference(
            @Parameter(description = "Preference ID", example = "1", required = true) @PathVariable Long id,
            @Parameter(description = "New enabled status", example = "true", required = true) @RequestParam boolean enabled) {
        NotificationPreferenceResponse response = preferenceService.togglePreference(id, enabled);
        return ResponseEntity.ok(ApiResponse.success("Notification preference toggled", response));
    }
}
