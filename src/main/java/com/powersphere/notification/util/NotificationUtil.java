package com.powersphere.notification.util;

import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.model.NotificationStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class NotificationUtil {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_FORMATTER) : null;
    }

    public static String getStatusDisplayName(NotificationStatus status) {
        if (status == null) return null;
        switch (status) {
            case PENDING: return "Pending";
            case SENT:    return "Sent";
            case FAILED:  return "Failed";
            case READ:    return "Read";
            default:      return status.name();
        }
    }

    public static String getPriorityDisplayName(NotificationPriority priority) {
        if (priority == null) return null;
        switch (priority) {
            case LOW:      return "Low";
            case MEDIUM:   return "Medium";
            case HIGH:     return "High";
            case CRITICAL: return "Critical";
            default:       return priority.name();
        }
    }

    public static boolean isHighPriority(NotificationPriority priority) {
        return priority == NotificationPriority.HIGH || priority == NotificationPriority.CRITICAL;
    }

    public static boolean canRetry(NotificationStatus status, int retryCount, int maxRetries) {
        return status == NotificationStatus.FAILED && retryCount < maxRetries;
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        if (localPart.length() <= 2) {
            return localPart.charAt(0) + "***@" + domain;
        }
        return localPart.substring(0, 2) + "***@" + domain;
    }

    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return phone;
        return phone.substring(0, phone.length() - 4).replaceAll(".", "*")
                + phone.substring(phone.length() - 4);
    }

    public static String truncateMessage(String message, int maxLength) {
        if (message == null) return null;
        if (message.length() <= maxLength) return message;
        return message.substring(0, maxLength) + "...";
    }
}
