package com.powersphere.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAlertRuleRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Event type is required")
    private String eventType;

    private String conditionExpression;

    private Double thresholdValue;

    private String comparisonOperator;

    @NotNull(message = "Priority is required")
    private String priority;

    private Boolean isActive;
}
