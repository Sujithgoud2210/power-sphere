package com.powersphere.billing.listener;

import com.powersphere.billing.entity.Bill;
import com.powersphere.billing.entity.BillHistory;
import com.powersphere.billing.event.BillCancelledEvent;
import com.powersphere.billing.event.BillGeneratedEvent;
import com.powersphere.billing.event.BillUpdatedEvent;
import com.powersphere.billing.repository.BillHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event listener that processes billing events and creates audit history records.
 * Handles bill generation, cancellation, and update events.
 */
@Component
public class BillEventListener {

    private static final Logger log = LoggerFactory.getLogger(BillEventListener.class);

    private final BillHistoryRepository billHistoryRepository;

    public BillEventListener(BillHistoryRepository billHistoryRepository) {
        this.billHistoryRepository = billHistoryRepository;
    }

    /**
     * Handles bill generated event by creating an audit history entry.
     */
    @EventListener
    @Transactional
    public void handleBillGeneratedEvent(BillGeneratedEvent event) {
        Bill bill = event.getBill();
        log.info("Bill generated: number={}, amount={}, consumer={}",
                bill.getBillNumber(), bill.getTotalAmount(), bill.getConsumerName());

        BillHistory history = BillHistory.builder()
                .action("GENERATED")
                .newStatus(bill.getStatus().name())
                .changeDescription("Bill generated for " + bill.getBillingMonth()
                        + "/" + bill.getBillingYear())
                .build();

        bill.addBillHistory(history);
        billHistoryRepository.save(history);
    }

    /**
     * Handles bill cancelled event by creating an audit history entry.
     */
    @EventListener
    @Transactional
    public void handleBillCancelledEvent(BillCancelledEvent event) {
        Bill bill = event.getBill();
        log.info("Bill cancelled: number={}, reason={}",
                bill.getBillNumber(), event.getReason());

        BillHistory history = BillHistory.builder()
                .action("CANCELLED")
                .previousStatus(event.getReason())
                .newStatus("CANCELLED")
                .changeDescription("Bill cancelled: " + event.getReason())
                .build();

        bill.addBillHistory(history);
        billHistoryRepository.save(history);
    }

    /**
     * Handles bill updated event by creating an audit history entry.
     */
    @EventListener
    @Transactional
    public void handleBillUpdatedEvent(BillUpdatedEvent event) {
        Bill bill = event.getBill();
        log.info("Bill updated: number={}, change={}",
                bill.getBillNumber(), event.getChangeDescription());

        BillHistory history = BillHistory.builder()
                .action("UPDATED")
                .newStatus(bill.getStatus().name())
                .changeDescription(event.getChangeDescription())
                .build();

        bill.addBillHistory(history);
        billHistoryRepository.save(history);
    }
}
