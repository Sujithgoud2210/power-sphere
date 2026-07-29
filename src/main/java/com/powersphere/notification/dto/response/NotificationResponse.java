package com.powersphere.notification.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;
    private String title;
    private String message;
    private Long recipientUser;
    private String recipientEmail;
    private String recipientPhone;
    private String notificationType;
    private String priority;
    private String status;
    private LocalDateTime scheduledTime;
    private LocalDateTime sentTime;
    private LocalDateTime readTime;
    private String channel;
    private Integer retryCount;
    private String remarks;
    private Long billId;
    private Long energyAlertId;
    private Long meterEventId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
