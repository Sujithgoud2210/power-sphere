package com.powersphere.notification.dto.request;

import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Message is required")
    private String message;

    private Long recipientUser;

    private String recipientEmail;

    private String recipientPhone;

    @NotNull(message = "Notification type is required")
    private NotificationChannel notificationType;

    @NotNull(message = "Priority is required")
    private NotificationPriority priority;

    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    private LocalDateTime scheduledTime;

    private String remarks;

    private Long billId;

    private Long energyAlertId;

    private Long meterEventId;
}
