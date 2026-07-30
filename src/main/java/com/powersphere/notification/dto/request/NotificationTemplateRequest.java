package com.powersphere.notification.dto.request;

import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;

/**
 * Request DTO for creating or updating a notification template.
 */
public class NotificationTemplateRequest {

    private String code;
    private String name;
    private String description;
    private NotificationType type;
    private NotificationChannel channel;
    private String subjectTemplate;
    private String bodyTemplate;
    private boolean active = true;
    private Long organizationId;

    // --- Getters and Setters ---

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public String getSubjectTemplate() { return subjectTemplate; }
    public void setSubjectTemplate(String subjectTemplate) { this.subjectTemplate = subjectTemplate; }

    public String getBodyTemplate() { return bodyTemplate; }
    public void setBodyTemplate(String bodyTemplate) { this.bodyTemplate = bodyTemplate; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
}
