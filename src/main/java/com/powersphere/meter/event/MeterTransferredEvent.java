package com.powersphere.meter.event;

import com.powersphere.meter.entity.SmartMeter;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.util.UUID;

@Getter
public class MeterTransferredEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final SmartMeter meter;
    private final UUID previousUserId;
    private final UUID newUserId;

    public MeterTransferredEvent(Object source, SmartMeter meter, UUID previousUserId, UUID newUserId) {
        super(source);
        this.meter = meter;
        this.previousUserId = previousUserId;
        this.newUserId = newUserId;
    }
}
