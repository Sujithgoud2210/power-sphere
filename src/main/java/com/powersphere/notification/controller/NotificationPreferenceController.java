package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.service.NotificationPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification-preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> createPreference(
            @Valid @RequestBody NotificationPreferenceRequest request) {
        NotificationPreferenceResponse response = preferenceService.createPreference(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification preference created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> updatePreference(
            @PathVariable Long id,
            @Valid @RequestBody NotificationPreferenceRequest request) {
        NotificationPreferenceResponse response = preferenceService.updatePreference(id, request);
        return ResponseEntity.ok(ApiResponse.success("Notification preference updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePreference(@PathVariable Long id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.ok(ApiResponse.success("Notification preference deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> getPreferenceById(@PathVariable Long id) {
        NotificationPreferenceResponse response = preferenceService.getPreferenceById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationPreferenceResponse>>> getPreferencesByUserId(
            @PathVariable Long userId) {
        List<NotificationPreferenceResponse> response = preferenceService.getPreferencesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<NotificationPreferenceResponse>> togglePreference(
            @PathVariable Long id,
            @RequestParam boolean enabled) {
        NotificationPreferenceResponse response = preferenceService.togglePreference(id, enabled);
        return ResponseEntity.ok(ApiResponse.success("Notification preference toggled", response));
    }
}
