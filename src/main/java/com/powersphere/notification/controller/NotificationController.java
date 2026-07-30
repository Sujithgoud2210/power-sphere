package com.powersphere.notification.controller;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.NotificationSearchRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.service.NotificationService;
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

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification creation, scheduling, delivery, and management endpoints")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create a new notification",
            description = "Creates a new notification with the specified type, priority, recipient, and content. The notification will be processed and delivered according to its configuration.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification created successfully", response));
    }

    @PostMapping("/schedule")
    @Operation(summary = "Schedule a notification",
            description = "Schedules a notification for future delivery. The notification will be stored and delivered at the scheduled time based on its configuration.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Notification scheduled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> scheduleNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification scheduled successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing notification",
            description = "Updates the content, priority, or status of an existing notification. Only notifications in a modifiable state can be updated.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(
            @Parameter(description = "Notification ID", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody UpdateNotificationRequest request) {
        NotificationResponse response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(ApiResponse.success("Notification updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification",
            description = "Deletes a notification by its ID. This is a soft delete that marks the notification as deleted without removing it from the database.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @Parameter(description = "Notification ID", example = "1", required = true) @PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID",
            description = "Retrieves detailed information about a specific notification by its unique identifier.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(
            @Parameter(description = "Notification ID", example = "1", required = true) @PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all notifications with pagination",
            description = "Retrieves a paginated list of all notifications with sorting. Supports page, size, sort field, and sort direction parameters.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notifications retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> getAllNotifications(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (createdAt, type, priority, status)", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<NotificationResponse> response = notificationService.getAllNotifications(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Mark notification as read",
            description = "Marks a notification as read by the recipient. Updates the read timestamp and status accordingly.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @Parameter(description = "Notification ID", example = "1", required = true) @PathVariable Long id) {
        NotificationResponse response = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", response));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a notification",
            description = "Cancels a scheduled or pending notification. Already delivered notifications cannot be cancelled.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Notification cancelled"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Notification not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Cannot cancel notification in current status")
    })
    public ResponseEntity<ApiResponse<NotificationResponse>> cancelNotification(
            @Parameter(description = "Notification ID", example = "1", required = true) @PathVariable Long id) {
        NotificationResponse response = notificationService.cancelNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Notification cancelled", response));
    }

    @PostMapping("/search")
    @Operation(summary = "Search notifications with filters",
            description = "Advanced search for notifications using filters like type, priority, status, date range, and recipient. Results are paginated.",
            tags = {"Notifications"})
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> searchNotifications(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Search criteria including filters and pagination", required = true)
            @RequestBody NotificationSearchRequest searchRequest) {
        PagedResponse<NotificationResponse> response = notificationService.searchNotifications(searchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
