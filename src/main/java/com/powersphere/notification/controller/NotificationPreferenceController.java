package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.service.NotificationPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * REST controller for managing user notification preferences.
 * Allows users to configure their notification channels, quiet hours,
 * and notification type preferences.
 */
@RestController
@RequestMapping("/api/v1/notification-preferences")
public class NotificationPreferenceController {

    private static final Logger log = LoggerFactory.getLogger(NotificationPreferenceController.class);

    private final NotificationPreferenceService preferenceService;

    public NotificationPreferenceController(NotificationPreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Retrieves notification preferences for a user.
     *
     * @param userId the user ID
     * @return the user's notification preferences
     */
    @GetMapping("/{userId}")
    public ResponseEntity<NotificationPreferenceResponse> getPreferences(@PathVariable Long userId) {
        log.debug("REST request to get preferences for userId={}", userId);
        NotificationPreferenceResponse response = preferenceService.getPreferences(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates notification preferences for a user.
     *
     * @param userId  the user ID
     * @param request the preference update request
     * @return the updated preferences
     */
    @PutMapping("/{userId}")
    public ResponseEntity<NotificationPreferenceResponse> updatePreferences(
            @PathVariable Long userId,
            @Valid @RequestBody NotificationPreferenceRequest request) {
        log.info("REST request to update preferences for userId={}", userId);
        NotificationPreferenceResponse response = preferenceService.updatePreferences(userId, request);
        return ResponseEntity.ok(response);
    }
}
