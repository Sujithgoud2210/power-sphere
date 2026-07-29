package com.powersphere.notification.entity;

import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Entity representing a reusable notification template. Templates define the
 * structure and default content for notifications, supporting variable substitution
 * and multi-channel content variations.
 */
@Entity
@Table(name = "notification_templates", indexes = {
    @Index(name = "idx_template_code", columnList = "code", unique = true),
    @Index(name = "idx_template_type_channel", columnList = "type, channel")
})
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(name = "subject_template", length = 500)
    private String subjectTemplate;

    @Lob
    @Column(name = "body_template", columnDefinition = "TEXT", nullable = false)
    private String bodyTemplate;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "organizationId")
    private Long organizationId;

    @Column(name = "createdBy")
    private Long createdBy;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Builder Pattern ---

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final NotificationTemplate template = new NotificationTemplate();

        public Builder id(Long id) { template.id = id; return this; }
        public Builder code(String code) { template.code = code; return this; }
        public Builder name(String name) { template.name = name; return this; }
        public Builder description(String description) { template.description = description; return this; }
        public Builder type(NotificationType type) { template.type = type; return this; }
        public Builder channel(NotificationChannel channel) { template.channel = channel; return this; }
        public Builder subjectTemplate(String subjectTemplate) { template.subjectTemplate = subjectTemplate; return this; }
        public Builder bodyTemplate(String bodyTemplate) { template.bodyTemplate = bodyTemplate; return this; }
        public Builder active(boolean active) { template.active = active; return this; }
        public Builder organizationId(Long organizationId) { template.organizationId = organizationId; return this; }
        public Builder createdBy(Long createdBy) { template.createdBy = createdBy; return this; }

        public NotificationTemplate build() {
            Objects.requireNonNull(template.code, "code must not be null");
            Objects.requireNonNull(template.name, "name must not be null");
            Objects.requireNonNull(template.type, "type must not be null");
            Objects.requireNonNull(template.channel, "channel must not be null");
            Objects.requireNonNull(template.bodyTemplate, "bodyTemplate must not be null");
            return template;
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTemplate that = (NotificationTemplate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NotificationTemplate.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("code='" + code + "'")
            .add("name='" + name + "'")
            .add("type=" + type)
            .add("channel=" + channel)
            .add("active=" + active)
            .toString();
    }
}
