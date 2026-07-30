package com.powersphere.billing.exception;

/**
 * Exception thrown when a tariff plan cannot be found.
 */
public class TariffPlanNotFoundException extends BillingException {

    public TariffPlanNotFoundException(Long id) {
        super("Tariff plan not found with id: " + id, "TARIFF_PLAN_NOT_FOUND");
    }

    public TariffPlanNotFoundException(String code) {
        super("Tariff plan not found with code: " + code, "TARIFF_PLAN_NOT_FOUND");
    }
}
