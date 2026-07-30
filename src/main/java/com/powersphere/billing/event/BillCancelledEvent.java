package com.powersphere.billing.event;

import com.powersphere.billing.entity.Bill;
import org.springframework.context.ApplicationEvent;

/**
 * Application event published when a bill has been cancelled.
 */
public class BillCancelledEvent extends ApplicationEvent {

    private final Bill bill;
    private final String reason;

    public BillCancelledEvent(Object source, Bill bill, String reason) {
        super(source);
        this.bill = bill;
        this.reason = reason;
    }

    public Bill getBill() {
        return bill;
    }

    public String getReason() {
        return reason;
    }
}
