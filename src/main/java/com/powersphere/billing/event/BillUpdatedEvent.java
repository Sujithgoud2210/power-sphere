package com.powersphere.billing.event;

import com.powersphere.billing.entity.Bill;
import org.springframework.context.ApplicationEvent;

/**
 * Application event published when an existing bill has been modified.
 */
public class BillUpdatedEvent extends ApplicationEvent {

    private final Bill bill;
    private final String changeDescription;

    public BillUpdatedEvent(Object source, Bill bill, String changeDescription) {
        super(source);
        this.bill = bill;
        this.changeDescription = changeDescription;
    }

    public Bill getBill() {
        return bill;
    }

    public String getChangeDescription() {
        return changeDescription;
    }
}
