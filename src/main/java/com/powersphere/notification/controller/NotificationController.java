package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.NotificationSearchRequest;
import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PageResponse;
import com.powersphere.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller exposing endpoints for managing notifications.
 * Provides full CRUD operations, status transitions, and search capabilities
 * for the notification system.
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Sends a single notification.
     *
     * @param request the notification send request
     * @return the created notification
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @Valid @RequestBody SendNotificationRequest request) {
        log.info("REST request to send notification: type={}, recipientId={}, channel={}",
                request.getType(), request.getRecipientId(), request.getChannel());
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Sends a batch of notifications.
     *
     * @param requests the list of notification send requests
     * @return the list of created notifications
     */
    @PostMapping("/batch")
    public ResponseEntity<List<NotificationResponse>> sendBatchNotifications(
            @Valid @RequestBody List<SendNotificationRequest> requests) {
        log.info("REST request to send batch of {} notifications", requests.size());
        List<NotificationResponse> responses = notificationService.sendBatchNotifications(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    /**
     * Retrieves a notification by its ID.
     *
     * @param id the notification ID
     * @return the notification
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        log.debug("REST request to get notification: id={}", id);
        NotificationResponse response = notificationService.getNotification(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a notification for a specific recipient.
     *
     * @param notificationId the notification ID
     * @param recipientId    the recipient user ID
     * @return the notification
     */
    @GetMapping("/{notificationId}/recipient/{recipientId}")
    public ResponseEntity<NotificationResponse> getNotificationForRecipient(
            @PathVariable Long notificationId,
            @PathVariable Long recipientId) {
        log.debug("REST request to get notification for recipient: id={}, recipientId={}",
                notificationId, recipientId);
        NotificationResponse response = notificationService.getNotificationForRecipient(
                notificationId, recipientId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing notification.
     *
     * @param id      the notification ID
     * @param request the update request
     * @return the updated notification
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationRequest request) {
        log.info("REST request to update notification: id={}", id);
        NotificationResponse response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param id the notification ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("REST request to delete notification: id={}", id);
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Marks a notification as read by the recipient.
     *
     * @param notificationId the notification ID
     * @param recipientId    the recipient user ID
     * @return the updated notification
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long notificationId,
            @RequestParam Long recipientId) {
        log.info("REST request to mark notification as read: id={}, recipientId={}",
                notificationId, recipientId);
        NotificationResponse response = notificationService.markAsRead(notificationId, recipientId);
        return ResponseEntity.ok(response);
    }

    /**
     * Archives a notification.
     *
     * @param id the notification ID
     * @return the updated notification
     */
    @PatchMapping("/{id}/archive")
    public ResponseEntity<NotificationResponse> archiveNotification(@PathVariable Long id) {
        log.info("REST request to archive notification: id={}", id);
        NotificationResponse response = notificationService.archiveNotification(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Archives all read notifications for a recipient.
     *
     * @param recipientId the recipient user ID
     * @return the count of archived notifications
     */
    @PostMapping("/archive-read")
    public ResponseEntity<Integer> archiveAllRead(@RequestParam Long recipientId) {
        log.info("REST request to archive all read notifications for recipientId={}", recipientId);
        int archived = notificationService.archiveAllRead(recipientId);
        return ResponseEntity.ok(archived);
    }

    /**
     * Cancels a pending notification.
     *
     * @param id the notification ID
     * @return the updated notification
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<NotificationResponse> cancelNotification(@PathVariable Long id) {
        log.info("REST request to cancel notification: id={}", id);
        NotificationResponse response = notificationService.cancelNotification(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Searches for notifications with filters and pagination.
     *
     * @param request the search request with filters
     * @return paginated search results
     */
    @PostMapping("/search")
    public ResponseEntity<PageResponse<NotificationResponse>> searchNotifications(
            @RequestBody NotificationSearchRequest request) {
        log.debug("REST request to search notifications");
        PageResponse<NotificationResponse> response = notificationService.searchNotifications(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all notifications for a specific recipient with pagination.
     *
     * @param recipientId the recipient user ID
     * @param page        the page number
     * @param size        the page size
     * @return paginated notifications
     */
    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<PageResponse<NotificationResponse>> getNotificationsByRecipient(
            @PathVariable Long recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("REST request to get notifications for recipient: recipientId={}, page={}, size={}",
                recipientId, page, size);
        PageResponse<NotificationResponse> response = notificationService.getNotificationsByRecipient(
                recipientId, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the unread notification count for a recipient.
     *
     * @param recipientId the recipient user ID
     * @return the unread count
     */
    @GetMapping("/unread-count/{recipientId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long recipientId) {
        long count = notificationService.getUnreadCount(recipientId);
        return ResponseEntity.ok(count);
    }
}
