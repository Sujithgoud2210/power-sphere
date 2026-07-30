package com.powersphere.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the notification module.
 * Binds to the 'powersphere.notification' prefix in application configuration.
 */
@Component
@ConfigurationProperties(prefix = "powersphere.notification")
public class NotificationProperties {

    /** Default retry interval in milliseconds for failed notifications */
    private long retryInterval = 300000L;

    /** Default scheduler interval in milliseconds for due notifications */
    private long schedulerInterval = 60000L;

    /** Maximum number of retry attempts for failed notifications */
    private int maxRetries = 3;

    /** Maximum number of notifications to process in a single batch */
    private int batchSize = 50;

    /** Whether to enable email notifications */
    private boolean emailEnabled = true;

    /** Whether to enable SMS notifications */
    private boolean smsEnabled = false;

    /** Whether to enable push notifications */
    private boolean pushEnabled = true;

    /** Whether to enable in-app notifications */
    private boolean inAppEnabled = true;

    // --- Getters and Setters ---

    public long getRetryInterval() { return retryInterval; }
    public void setRetryInterval(long retryInterval) { this.retryInterval = retryInterval; }

    public long getSchedulerInterval() { return schedulerInterval; }
    public void setSchedulerInterval(long schedulerInterval) { this.schedulerInterval = schedulerInterval; }

    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }

    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }

    public boolean isEmailEnabled() { return emailEnabled; }
    public void setEmailEnabled(boolean emailEnabled) { this.emailEnabled = emailEnabled; }

    public boolean isSmsEnabled() { return smsEnabled; }
    public void setSmsEnabled(boolean smsEnabled) { this.smsEnabled = smsEnabled; }

    public boolean isPushEnabled() { return pushEnabled; }
    public void setPushEnabled(boolean pushEnabled) { this.pushEnabled = pushEnabled; }

    public boolean isInAppEnabled() { return inAppEnabled; }
    public void setInAppEnabled(boolean inAppEnabled) { this.inAppEnabled = inAppEnabled; }
}
