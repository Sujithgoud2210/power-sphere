package com.powersphere.energy.listener;

import com.powersphere.energy.event.ConsumptionCalculatedEvent;
import com.powersphere.energy.event.EnergyReadingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class EnergyReadingEventListener {

    @TransactionalEventListener
    public void handleEnergyReadingCreated(EnergyReadingCreatedEvent event) {
        log.info("Energy reading created: ID={}, Meter={}, Reading={}",
                event.getEnergyReading().getId(),
                event.getEnergyReading().getMeterId(),
                event.getEnergyReading().getCurrentReading());
    }

    @EventListener
    public void handleConsumptionCalculated(ConsumptionCalculatedEvent event) {
        log.info("Consumption calculated: Meter={}, Consumption={} {}",
                event.getMeterId(),
                event.getConsumption(),
                "kWh");
    }
}
