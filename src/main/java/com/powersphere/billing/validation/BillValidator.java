package com.powersphere.billing.validation;

import com.powersphere.billing.dto.request.GenerateBillRequest;
import com.powersphere.billing.enums.BillStatus;
import com.powersphere.billing.entity.Bill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator for billing operations. Enforces business rules for bill generation,
 * updates, and status transitions.
 */
@Component
public class BillValidator {

    private static final int MAX_BILL_NUMBER_LENGTH = 50;

    /**
     * Validates a bill generation request.
     *
     * @param request the generate bill request
     * @return list of validation error messages, empty if valid
     */
    public List<String> validateGenerateRequest(GenerateBillRequest request) {
        List<String> errors = new ArrayList<>();

        if (request == null) {
            errors.add("Generate bill request must not be null");
            return errors;
        }

        // Meter validation
        if (request.getMeterId() == null) {
            errors.add("Meter ID is required");
        }

        // Reading validation
        if (request.getPreviousReading() == null) {
            errors.add("Previous reading is required");
        }

        if (request.getCurrentReading() == null) {
            errors.add("Current reading is required");
        }

        if (request.getPreviousReading() != null && request.getCurrentReading() != null) {
            if (request.getCurrentReading().compareTo(request.getPreviousReading()) < 0) {
                errors.add("Current reading must be greater than or equal to previous reading");
            }
        }

        // Month/Year validation
        if (request.getBillingMonth() < 1 || request.getBillingMonth() > 12) {
            errors.add("Billing month must be between 1 and 12");
        }

        if (request.getBillingYear() < 2000 || request.getBillingYear() > 2100) {
            errors.add("Billing year must be between 2000 and 2100");
        }

        // Due date validation
        if (request.getDueDate() == null) {
            errors.add("Due date is required");
        } else if (request.getDueDate().isBefore(LocalDate.now())) {
            errors.add("Due date must not be in the past");
        }

        return errors;
    }

    /**
     * Validates bill status transitions.
     *
     * @param bill the current bill
     * @param newStatus the desired new status
     * @throws IllegalStateException if the transition is not allowed
     */
    public void validateStatusTransition(Bill bill, BillStatus newStatus) {
        BillStatus currentStatus = bill.getStatus();

        if (currentStatus == newStatus) {
            return;
        }

        switch (newStatus) {
            case CANCELLED:
                if (currentStatus == BillStatus.PAID) {
                    throw new IllegalStateException(
                            "Cannot cancel a paid bill. Refund must be processed first.");
                }
                if (currentStatus == BillStatus.CANCELLED) {
                    throw new IllegalStateException("Bill is already cancelled.");
                }
                break;
            case PAID:
                if (currentStatus == BillStatus.CANCELLED) {
                    throw new IllegalStateException("Cannot mark a cancelled bill as paid.");
                }
                break;
            case OVERDUE:
                if (currentStatus == BillStatus.PAID || currentStatus == BillStatus.CANCELLED) {
                    throw new IllegalStateException(
                            "Cannot mark a " + currentStatus + " bill as overdue.");
                }
                break;
            default:
                // All other transitions are allowed
                break;
        }
    }

    /**
     * Validates that a bill number is not a duplicate.
     *
     * @param billNumber the bill number to check
     * @param exists whether the bill number already exists
     */
    public void validateBillNumberUniqueness(String billNumber, boolean exists) {
        if (exists) {
            throw new IllegalArgumentException(
                    "Bill number '" + billNumber + "' already exists");
        }
        if (billNumber == null || billNumber.isBlank()) {
            throw new IllegalArgumentException("Bill number must not be blank");
        }
        if (billNumber.length() > MAX_BILL_NUMBER_LENGTH) {
            throw new IllegalArgumentException(
                    "Bill number must not exceed " + MAX_BILL_NUMBER_LENGTH + " characters");
        }
    }
}
