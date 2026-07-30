package com.powersphere.energy.event;

import com.powersphere.energy.entity.EnergyReading;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EnergyReadingCreatedEvent extends ApplicationEvent {

    private final EnergyReading energyReading;

    public EnergyReadingCreatedEvent(Object source, EnergyReading energyReading) {
        super(source);
        this.energyReading = energyReading;
    }
}
