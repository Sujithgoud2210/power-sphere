package com.powersphere.billing.event;

import com.powersphere.billing.entity.Bill;
import org.springframework.context.ApplicationEvent;

/**
 * Application event published when a new bill has been successfully generated.
 * Other modules (notification, dashboard, etc.) can react to this event.
 */
public class BillGeneratedEvent extends ApplicationEvent {

    private final Bill bill;

    public BillGeneratedEvent(Object source, Bill bill) {
        super(source);
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }
}
