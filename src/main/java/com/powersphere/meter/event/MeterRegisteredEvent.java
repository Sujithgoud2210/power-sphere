package com.powersphere.meter.event;

import com.powersphere.meter.entity.SmartMeter;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;

@Getter
public class MeterRegisteredEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final SmartMeter meter;

    public MeterRegisteredEvent(Object source, SmartMeter meter) {
        super(source);
        this.meter = meter;
    }
}
