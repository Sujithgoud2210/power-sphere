package com.powersphere.notification.exception;

public class AlertRuleNotFoundException extends RuntimeException {

    public AlertRuleNotFoundException(Long id) {
        super("Alert rule not found with id: " + id);
    }

    public AlertRuleNotFoundException(String message) {
        super(message);
    }
}
