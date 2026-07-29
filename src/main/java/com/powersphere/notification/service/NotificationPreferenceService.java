package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;

/**
 * Service interface for managing user notification preferences. Controls
 * per-user notification channel enablement, quiet hours, and notification type
 * suppression.
 */
public interface NotificationPreferenceService {

    /**
     * Retrieves the notification preferences for a given user.
     * Creates default preferences if none exist.
     *
     * @param userId the user ID
     * @return the user's notification preferences
     */
    NotificationPreferenceResponse getPreferences(Long userId);

    /**
     * Updates the notification preferences for a given user.
     * Creates new preferences if none exist.
     *
     * @param userId  the user ID
     * @param request the preference update request
     * @return the updated notification preferences
     */
    NotificationPreferenceResponse updatePreferences(Long userId, NotificationPreferenceRequest request);

    /**
     * Checks whether a user has enabled the given notification channel.
     *
     * @param userId  the user ID
     * @param channel the channel name as a string
     * @return true if the channel is enabled
     */
    boolean isChannelEnabled(Long userId, String channel);

    /**
     * Checks whether a user has enabled the given notification type.
     *
     * @param userId the user ID
     * @param type   the notification type name as a string
     * @return true if the type is enabled
     */
    boolean isNotificationTypeEnabled(Long userId, String type);
}
