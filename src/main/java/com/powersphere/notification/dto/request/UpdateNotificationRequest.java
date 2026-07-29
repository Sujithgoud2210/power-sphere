package com.powersphere.notification.dto.request;

import com.powersphere.notification.enums.NotificationPriority;
import com.powersphere.notification.enums.NotificationStatus;

import java.time.LocalDateTime;

/**
 * Request DTO for updating an existing notification.
 */
public class UpdateNotificationRequest {

    private NotificationStatus status;
    private NotificationPriority priority;
    private String subject;
    private String content;
    private LocalDateTime scheduledFor;
    private Integer maxRetries;

    // --- Getters and Setters ---

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public NotificationPriority getPriority() { return priority; }
    public void setPriority(NotificationPriority priority) { this.priority = priority; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getScheduledFor() { return scheduledFor; }
    public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }

    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
}
