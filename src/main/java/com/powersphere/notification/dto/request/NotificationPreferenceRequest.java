package com.powersphere.notification.dto.request;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Request DTO for updating user notification preferences.
 */
public class NotificationPreferenceRequest {

    private boolean emailEnabled = true;
    private boolean smsEnabled = false;
    private boolean pushEnabled = true;
    private boolean inAppEnabled = true;
    private String digestFrequency;
    private LocalTime quietHoursStart;
    private LocalTime quietHoursEnd;
    private boolean quietHoursEnabled = false;
    private Set<String> disabledNotificationTypes = new HashSet<>();

    // --- Getters and Setters ---

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
}
