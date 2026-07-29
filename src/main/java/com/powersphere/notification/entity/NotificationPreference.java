package com.powersphere.notification.entity;

import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Entity representing user notification preferences. Controls which notification
 * types are enabled, preferred delivery channels, and quiet hours configuration.
 */
@Entity
@Table(name = "notification_preferences", indexes = {
    @Index(name = "idx_pref_user", columnList = "userId", unique = true),
    @Index(name = "idx_pref_org_user", columnList = "organizationId, userId")
})
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "organizationId")
    private Long organizationId;

    @Column(name = "email_enabled", nullable = false)
    private boolean emailEnabled = true;

    @Column(name = "sms_enabled", nullable = false)
    private boolean smsEnabled = false;

    @Column(name = "push_enabled", nullable = false)
    private boolean pushEnabled = true;

    @Column(name = "in_app_enabled", nullable = false)
    private boolean inAppEnabled = true;

    @Column(name = "digest_frequency", length = 20)
    private String digestFrequency;

    @Column(name = "quiet_hours_start")
    private LocalTime quietHoursStart;

    @Column(name = "quiet_hours_end")
    private LocalTime quietHoursEnd;

    @Column(name = "quiet_hours_enabled", nullable = false)
    private boolean quietHoursEnabled = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "disabled_notification_types")
    private Set<String> disabledNotificationTypes = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "preferred_channels")
    private Set<String> preferredChannels = new HashSet<>();

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
        private final NotificationPreference preference = new NotificationPreference();

        public Builder id(Long id) { preference.id = id; return this; }
        public Builder userId(Long userId) { preference.userId = userId; return this; }
        public Builder organizationId(Long organizationId) { preference.organizationId = organizationId; return this; }
        public Builder emailEnabled(boolean emailEnabled) { preference.emailEnabled = emailEnabled; return this; }
        public Builder smsEnabled(boolean smsEnabled) { preference.smsEnabled = smsEnabled; return this; }
        public Builder pushEnabled(boolean pushEnabled) { preference.pushEnabled = pushEnabled; return this; }
        public Builder inAppEnabled(boolean inAppEnabled) { preference.inAppEnabled = inAppEnabled; return this; }
        public Builder digestFrequency(String digestFrequency) { preference.digestFrequency = digestFrequency; return this; }
        public Builder quietHoursStart(LocalTime quietHoursStart) { preference.quietHoursStart = quietHoursStart; return this; }
        public Builder quietHoursEnd(LocalTime quietHoursEnd) { preference.quietHoursEnd = quietHoursEnd; return this; }
        public Builder quietHoursEnabled(boolean quietHoursEnabled) { preference.quietHoursEnabled = quietHoursEnabled; return this; }
        public Builder disabledNotificationTypes(Set<String> disabledNotificationTypes) { preference.disabledNotificationTypes = disabledNotificationTypes; return this; }
        public Builder preferredChannels(Set<String> preferredChannels) { preference.preferredChannels = preferredChannels; return this; }

        public NotificationPreference build() {
            Objects.requireNonNull(preference.userId, "userId must not be null");
            return preference;
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }

    public boolean isSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }

    public boolean isPushEnabled() { return pushEnabled; }
    public void setPushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; }

    public boolean isInAppEnabled() { return inAppEnabled; }
    public void setInAppEnabled(boolean inAppEnabled) { this.inAppEnabled = inAppEnabled; }

    public String getDigestFrequency() { return digestFrequency; }
    public void setDigestFrequency(String digestFrequency) { this.digestFrequency = digestFrequency; }

    public LocalTime getQuietHoursStart() { return quietHoursStart; }
    public void setQuietHoursStart(LocalTime quietHoursStart) { this.quietHoursStart = quietHoursStart; }

    public LocalTime getQuietHoursEnd() { return quietHoursEnd; }
    public void setQuietHoursEnd(LocalTime quietHoursEnd) { this.quietHoursEnd = quietHoursEnd; }

    public boolean isQuietHoursEnabled() { return quietHoursEnabled; }
    public void setQuietHoursEnabled(boolean quietHoursEnabled) { this.quietHoursEnabled = quietHoursEnabled; }

    public Set<String> getDisabledNotificationTypes() { return disabledNotificationTypes; }
    public void setDisabledNotificationTypes(Set<String> disabledNotificationTypes) { this.disabledNotificationTypes = disabledNotificationTypes; }

    public Set<String> getPreferredChannels() { return preferredChannels; }
    public void setPreferredChannels(Set<String> preferredChannels) { this.preferredChannels = preferredChannels; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // --- Domain methods ---

    /**
     * Determines if the given notification channel is enabled for this user.
     */
    public boolean isChannelEnabled(NotificationChannel channel) {
        return switch (channel) {
            case EMAIL -> emailEnabled;
            case SMS -> smsEnabled;
            case PUSH -> pushEnabled;
            case IN_APP -> inAppEnabled;
            default -> true;
        };
    }

    /**
     * Checks if the given notification type is not disabled by the user.
     */
    public boolean isTypeEnabled(NotificationType type) {
        return !disabledNotificationTypes.contains(type.name());
    }

    /**
     * Determines if the current time falls within the user's configured quiet hours.
     */
    public boolean isInQuietHours() {
        if (!quietHoursEnabled || quietHoursStart == null || quietHoursEnd == null) {
            return false;
        }
        LocalTime now = LocalTime.now();
        if (quietHoursStart.isBefore(quietHoursEnd)) {
            return !now.isBefore(quietHoursStart) && !now.isAfter(quietHoursEnd);
        } else {
            return !now.isBefore(quietHoursStart) || !now.isAfter(quietHoursEnd);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationPreference that = (NotificationPreference) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NotificationPreference.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("userId=" + userId)
            .add("emailEnabled=" + emailEnabled)
            .add("smsEnabled=" + smsEnabled)
            .add("pushEnabled=" + pushEnabled)
            .add("inAppEnabled=" + inAppEnabled)
            .toString();
    }
}
