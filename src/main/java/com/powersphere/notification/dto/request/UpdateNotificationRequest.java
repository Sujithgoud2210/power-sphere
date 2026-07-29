package com.powersphere.notification.dto.request;

import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNotificationRequest {

    private String title;

    private String message;

    private Long recipientUser;

    private String recipientEmail;

    private String recipientPhone;

    private NotificationChannel notificationType;

    private NotificationPriority priority;

    private NotificationChannel channel;

    private LocalDateTime scheduledTime;

    private String remarks;

    private Long billId;

    private Long energyAlertId;

    private Long meterEventId;
}
