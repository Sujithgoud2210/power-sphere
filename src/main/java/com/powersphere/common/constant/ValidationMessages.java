package com.powersphere.common.constant;

/**
 * Centralized validation message constants.
 * <p>
 * Provides consistent validation messages across all modules.
 * Messages follow a pattern of {module}.{field}.{validationType}
 * for easy internationalization support in the future.
 */
public final class ValidationMessages {

    private ValidationMessages() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ===== Common Validation Messages =====
    public static final String REQUIRED_FIELD = "{field} is required";
    public static final String INVALID_FORMAT = "Invalid format for {field}";
    public static final String INVALID_LENGTH = "{field} length must be between {min} and {max} characters";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_PHONE = "Invalid phone number format";

    // ===== Authentication Module =====
    public static final String AUTH_USERNAME_REQUIRED = "Username is required";
    public static final String AUTH_PASSWORD_REQUIRED = "Password is required";
    public static final String AUTH_PASSWORD_MIN_LENGTH = "Password must be at least 8 characters";
    public static final String AUTH_PASSWORD_MAX_LENGTH = "Password must not exceed 128 characters";
    public static final String AUTH_INVALID_CREDENTIALS = "Invalid username or password";

    // ===== Users Module =====
    public static final String USER_NAME_REQUIRED = "User name is required";
    public static final String USER_EMAIL_REQUIRED = "Email is required";
    public static final String USER_EMAIL_INVALID = "Invalid email format";

    // ===== Organization Module =====
    public static final String ORG_NAME_REQUIRED = "Organization name is required";
    public static final String ORG_TAX_ID_INVALID = "Invalid tax identification number";

    // ===== Energy Module =====
    public static final String ENERGY_VALUE_INVALID = "Invalid energy consumption value";
    public static final String ENERGY_UNIT_REQUIRED = "Energy unit is required";

    // ===== Meter Module =====
    public static final String METER_SERIAL_REQUIRED = "Meter serial number is required";
    public static final String METER_TYPE_INVALID = "Invalid meter type";

    // ===== Billing Module =====
    public static final String BILL_AMOUNT_INVALID = "Invalid billing amount";
    public static final String BILL_CURRENCY_REQUIRED = "Currency is required";

    // ===== Notification Module =====
    public static final String NOTIFICATION_TYPE_INVALID = "Invalid notification type";
    public static final String NOTIFICATION_CHANNEL_INVALID = "Invalid notification channel";

    // ===== Reports Module =====
    public static final String REPORT_TYPE_INVALID = "Invalid report type";
    public static final String REPORT_DATE_RANGE_INVALID = "Invalid date range for report";
}
