package com.powersphere.notification.entity;

import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationPriority;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.enums.NotificationType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Core entity representing a notification within the PowerSphere system.
 * Captures all metadata necessary for processing, delivering, and tracking
 * notifications across multiple channels.
 */
@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_status", columnList = "status"),
    @Index(name = "idx_notification_recipient", columnList = "recipientId"),
    @Index(name = "idx_notification_type_status", columnList = "type, status"),
    @Index(name = "idx_notification_created_at", columnList = "createdAt"),
    @Index(name = "idx_notification_scheduled_for", columnList = "scheduledFor")
})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private NotificationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private NotificationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(name = "subject", length = 500)
    private String subject;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "senderId")
    private Long senderId;

    @Column(name = "senderEmail", length = 255)
    private String senderEmail;

    @Column(name = "senderName", length = 255)
    private String senderName;

    @Column(name = "recipientId", nullable = false)
    private Long recipientId;

    @Column(name = "recipientEmail", length = 255)
    private String recipientEmail;

    @Column(name = "recipientPhone", length = 50)
    private String recipientPhone;

    @Column(name = "recipientName", length = 255)
    private String recipientName;

    @Column(name = "templateCode", length = 100)
    private String templateCode;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "notification_metadata",
        joinColumns = @JoinColumn(name = "notification_id")
    )
    @MapKeyColumn(name = "meta_key", length = 255)
    @Column(name = "meta_value", columnDefinition = "TEXT")
    private Map<String, String> metadata = new HashMap<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "notification_attachments",
        joinColumns = @JoinColumn(name = "notification_id")
    )
    @Column(name = "attachment_url", length = 2048)
    private List<String> attachments = new ArrayList<>();

    @Column(name = "sentAt")
    private LocalDateTime sentAt;

    @Column(name = "deliveredAt")
    private LocalDateTime deliveredAt;

    @Column(name = "readAt")
    private LocalDateTime readAt;

    @Column(name = "errorMessage", length = 2000)
    private String errorMessage;

    @Column(name = "retryCount", nullable = false)
    private int retryCount;

    @Column(name = "maxRetries", nullable = false)
    private int maxRetries = 3;

    @Column(name = "scheduledFor")
    private LocalDateTime scheduledFor;

    @Column(name = "organizationId")
    private Long organizationId;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = NotificationStatus.PENDING;
        }
        if (this.priority == null) {
            this.priority = NotificationPriority.MEDIUM;
        }
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
        private final Notification notification = new Notification();

        public Builder id(Long id) { notification.id = id; return this; }
        public Builder type(NotificationType type) { notification.type = type; return this; }
        public Builder status(NotificationStatus status) { notification.status = status; return this; }
        public Builder priority(NotificationPriority priority) { notification.priority = priority; return this; }
        public Builder channel(NotificationChannel channel) { notification.channel = channel; return this; }
        public Builder subject(String subject) { notification.subject = subject; return this; }
        public Builder content(String content) { notification.content = content; return this; }
        public Builder senderId(Long senderId) { notification.senderId = senderId; return this; }
        public Builder senderEmail(String senderEmail) { notification.senderEmail = senderEmail; return this; }
        public Builder senderName(String senderName) { notification.senderName = senderName; return this; }
        public Builder recipientId(Long recipientId) { notification.recipientId = recipientId; return this; }
        public Builder recipientEmail(String recipientEmail) { notification.recipientEmail = recipientEmail; return this; }
        public Builder recipientPhone(String recipientPhone) { notification.recipientPhone = recipientPhone; return this; }
        public Builder recipientName(String recipientName) { notification.recipientName = recipientName; return this; }
        public Builder templateCode(String templateCode) { notification.templateCode = templateCode; return this; }
        public Builder metadata(Map<String, String> metadata) { notification.metadata = metadata; return this; }
        public Builder attachments(List<String> attachments) { notification.attachments = attachments; return this; }
        public Builder maxRetries(int maxRetries) { notification.maxRetries = maxRetries; return this; }
        public Builder scheduledFor(LocalDateTime scheduledFor) { notification.scheduledFor = scheduledFor; return this; }
        public Builder organizationId(Long organizationId) { notification.organizationId = organizationId; return this; }

        public Notification build() {
            Objects.requireNonNull(notification.type, "type must not be null");
            Objects.requireNonNull(notification.recipientId, "recipientId must not be null");
            Objects.requireNonNull(notification.channel, "channel must not be null");
            return notification;
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public NotificationPriority getPriority() { return priority; }
    public void setPriority(NotificationPriority priority) { this.priority = priority; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    public LocalDateTime getScheduledFor() { return scheduledFor; }
    public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // --- Domain methods ---

    /**
     * Marks the notification as sent with the current timestamp.
     */
    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * Marks the notification as delivered with the current timestamp.
     */
    public void markDelivered() {
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    /**
     * Marks the notification as read/acknowledged by the recipient.
     */
    public void markRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Marks the notification as failed with an associated error message.
     *
     * @param errorMessage the reason for failure
     */
    public void markFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    /**
     * Increments the retry count and sets status back to retrying.
     *
     * @return true if retries remain, false if max retries exceeded
     */
    public boolean incrementRetry() {
        this.retryCount++;
        if (this.retryCount >= this.maxRetries) {
            this.status = NotificationStatus.FAILED;
            return false;
        }
        this.status = NotificationStatus.RETRYING;
        return true;
    }

    /**
     * Archives this notification.
     */
    public void archive() {
        this.status = NotificationStatus.ARCHIVED;
    }

    /**
     * Cancels this notification if it hasn't been sent yet.
     *
     * @return true if cancelled, false if already sent or in terminal state
     */
    public boolean cancel() {
        if (this.status == NotificationStatus.PENDING || this.status == NotificationStatus.RETRYING) {
            this.status = NotificationStatus.CANCELLED;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Notification.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("type=" + type)
            .add("status=" + status)
            .add("priority=" + priority)
            .add("channel=" + channel)
            .add("subject='" + subject + "'")
            .add("recipientId=" + recipientId)
            .add("createdAt=" + createdAt)
            .toString();
    }
}
