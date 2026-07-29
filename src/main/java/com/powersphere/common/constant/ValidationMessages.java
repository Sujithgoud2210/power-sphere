package com.powersphere.common.constant;

public final class ValidationMessages {

    private ValidationMessages() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static final String REQUIRED_FIELD = "{field} is required";
    public static final String INVALID_FORMAT = "Invalid {field} format";
    public static final String INVALID_LENGTH = "{field} length is invalid";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_PHONE = "Invalid phone number format";

    // Authentication messages
    public static final String AUTH_USERNAME_REQUIRED = "Username is required";
    public static final String AUTH_PASSWORD_REQUIRED = "Password is required";
    public static final String AUTH_PASSWORD_MIN_LENGTH = "Password must be at least 8 characters";
    public static final String AUTH_PASSWORD_MAX_LENGTH = "Password must not exceed 100 characters";
    public static final String AUTH_INVALID_CREDENTIALS = "Invalid username or password";

    // User messages
    public static final String USER_NAME_REQUIRED = "User name is required";
    public static final String USER_EMAIL_REQUIRED = "Email is required";
    public static final String USER_EMAIL_INVALID = "Invalid email format";

    // Organization messages
    public static final String ORG_NAME_REQUIRED = "Organization name is required";
    public static final String ORG_TAX_ID_INVALID = "Invalid tax ID format";

    // Energy messages
    public static final String ENERGY_VALUE_INVALID = "Invalid energy value";
    public static final String ENERGY_UNIT_REQUIRED = "Energy unit is required";

    // Meter messages
    public static final String METER_SERIAL_REQUIRED = "Meter serial number is required";
    public static final String METER_TYPE_INVALID = "Invalid meter type";
    public static final String METER_NUMBER_REQUIRED = "Meter number is required";
    public static final String METER_MODEL_REQUIRED = "Meter model is required";
    public static final String METER_MANUFACTURER_REQUIRED = "Meter manufacturer is required";

    // Billing messages
    public static final String BILL_AMOUNT_INVALID = "Invalid billing amount";
    public static final String BILL_CURRENCY_REQUIRED = "Currency is required";

    // Notification messages
    public static final String NOTIFICATION_TYPE_INVALID = "Invalid notification type";
    public static final String NOTIFICATION_CHANNEL_INVALID = "Invalid notification channel";

    // Report messages
    public static final String REPORT_TYPE_INVALID = "Invalid report type";
    public static final String REPORT_DATE_RANGE_INVALID = "Invalid date range for report";
}
