package com.powersphere.energy.event;

import com.powersphere.energy.dto.response.ConsumptionResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public class ConsumptionCalculatedEvent extends ApplicationEvent {

    private final Long meterId;
    private final BigDecimal consumption;
    private final ConsumptionResponse consumptionResponse;

    public ConsumptionCalculatedEvent(Object source, Long meterId, BigDecimal consumption,
                                      ConsumptionResponse consumptionResponse) {
        super(source);
        this.meterId = meterId;
        this.consumption = consumption;
        this.consumptionResponse = consumptionResponse;
    }
}
