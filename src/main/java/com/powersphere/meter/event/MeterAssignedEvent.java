package com.powersphere.meter.event;

import com.powersphere.meter.entity.SmartMeter;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.UUID;

@Getter
public class MeterAssignedEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final SmartMeter meter;
    private final UUID assignedUserId;

    public MeterAssignedEvent(Object source, SmartMeter meter, UUID assignedUserId) {
        super(source);
        this.meter = meter;
        this.assignedUserId = assignedUserId;
    }
}
