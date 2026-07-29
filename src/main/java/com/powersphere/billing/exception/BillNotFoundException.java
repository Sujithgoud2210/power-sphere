package com.powersphere.billing.exception;

/**
 * Exception thrown when a requested bill resource cannot be found.
 */
public class BillNotFoundException extends BillingException {

    public BillNotFoundException(Long id) {
        super("Bill not found with id: " + id, "BILL_NOT_FOUND");
    }

    public BillNotFoundException(String billNumber) {
        super("Bill not found with number: " + billNumber, "BILL_NOT_FOUND");
    }
}
