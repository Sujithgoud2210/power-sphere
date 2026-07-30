package com.powersphere.billing.enums;

/**
 * Represents the lifecycle states of a bill within the PowerSphere billing system.
 */
public enum BillStatus {

    /** Bill has been generated and is pending delivery */
    GENERATED,

    /** Bill has been sent to the customer */
    SENT,

    /** Bill has been partially paid by the customer */
    PARTIALLY_PAID,

    /** Bill has been fully paid */
    PAID,

    /** Bill payment is overdue past the due date */
    OVERDUE,

    /** Bill has been cancelled/voided */
    CANCELLED
}
