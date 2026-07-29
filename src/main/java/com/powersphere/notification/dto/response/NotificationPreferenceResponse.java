package com.powersphere.notification.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Response DTO for user notification preferences.
 */
public class NotificationPreferenceResponse {

    private Long id;
    private Long userId;
    private Long organizationId;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
    private boolean inAppEnabled;
    private String digestFrequency;
    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;
    private boolean quietHoursEnabled;
    private Set<String> disabledNotificationTypes = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
