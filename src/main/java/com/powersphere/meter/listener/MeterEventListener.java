package com.powersphere.meter.listener;

import com.powersphere.meter.event.MeterAssignedEvent;
import com.powersphere.meter.event.MeterRegisteredEvent;
import com.powersphere.meter.event.MeterTransferredEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeterEventListener {

    private static final Logger log = LoggerFactory.getLogger(MeterEventListener.class);

    @EventListener
    public void handleMeterRegistered(MeterRegisteredEvent event) {
        log.info("Meter registered event received - Meter: {} ({}). " +
                        "Placeholder for future integration (e.g., Kafka notification, IoT provisioning).",
                event.getMeter().getMeterNumber(), event.getMeter().getId());
    }

    @EventListener
    public void handleMeterAssigned(MeterAssignedEvent event) {
        log.info("Meter assigned event received - Meter: {} assigned to User: {}. " +
                        "Placeholder for future integration (e.g., Kafka notification, user notification).",
                event.getMeter().getMeterNumber(), event.getAssignedUserId());
    }

    @EventListener
    public void handleMeterTransferred(MeterTransferredEvent event) {
        log.info("Meter transferred event received - Meter: {} transferred from User: {} to User: {}. " +
                        "Placeholder for future integration (e.g., Kafka notification, billing update).",
                event.getMeter().getMeterNumber(), event.getPreviousUserId(), event.getNewUserId());
    }
}
