package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.NotificationSearchRequest;
import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PageResponse;

import java.util.List;

/**
 * Service interface for managing notifications within the PowerSphere system.
 * Defines operations for sending, updating, tracking, and managing notifications
 * throughout their lifecycle.
 */
public interface NotificationService {

    /**
     * Sends a new notification based on the provided request.
     * If the request contains a template code, the template will be resolved
     * and the content rendered before sending.
     *
     * @param request the notification send request
     * @return the created notification response
     */
    NotificationResponse sendNotification(SendNotificationRequest request);

    /**
     * Sends a batch of notifications in a single operation.
     *
     * @param requests the list of notification send requests
     * @return the list of created notification responses
     */
    List<NotificationResponse> sendBatchNotifications(List<SendNotificationRequest> requests);

    /**
     * Retrieves a notification by its ID.
     *
     * @param id the notification ID
     * @return the notification response
     */
    NotificationResponse getNotification(Long id);

    /**
     * Retrieves a notification by its ID and recipient ID, ensuring
     * the recipient can only access their own notifications.
     *
     * @param notificationId the notification ID
     * @param recipientId    the recipient user ID
     * @return the notification response
     */
    NotificationResponse getNotificationForRecipient(Long notificationId, Long recipientId);

    /**
     * Updates an existing notification with the provided changes.
     *
     * @param id      the notification ID to update
     * @param request the update request with fields to modify
     * @return the updated notification response
     */
    NotificationResponse updateNotification(Long id, UpdateNotificationRequest request);

    /**
     * Deletes a notification by its ID.
     *
     * @param id the notification ID to delete
     */
    void deleteNotification(Long id);

    /**
     * Marks a notification as read by the recipient.
     *
     * @param notificationId the notification ID
     * @param recipientId    the recipient user ID
     * @return the updated notification response
     */
    NotificationResponse markAsRead(Long notificationId, Long recipientId);

    /**
     * Archives a notification.
     *
     * @param id the notification ID to archive
     * @return the updated notification response
     */
    NotificationResponse archiveNotification(Long id);

    /**
     * Archives all read notifications for a given recipient.
     *
     * @param recipientId the recipient user ID
     * @return the number of archived notifications
     */
    int archiveAllRead(Long recipientId);

    /**
     * Cancels a pending notification.
     *
     * @param id the notification ID to cancel
     * @return the updated notification response
     */
    NotificationResponse cancelNotification(Long id);

    /**
     * Searches for notifications based on the provided filter criteria.
     *
     * @param request the search request with filter and pagination parameters
     * @return a paginated response of matching notifications
     */
    PageResponse<NotificationResponse> searchNotifications(NotificationSearchRequest request);

    /**
     * Retrieves all notifications for a specific recipient with pagination.
     *
     * @param recipientId the recipient user ID
     * @param page        the page number (0-indexed)
     * @param size        the page size
     * @return a paginated response of notifications
     */
    PageResponse<NotificationResponse> getNotificationsByRecipient(Long recipientId, int page, int size);

    /**
     * Retrieves all unread notification count for a recipient.
     *
     * @param recipientId the recipient user ID
     * @return the count of unread notifications
     */
    long getUnreadCount(Long recipientId);
}
