package com.powersphere.notification.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAlertRuleRequest {

    private String name;

    private String description;

    private String eventType;

    private String conditionExpression;

    private Double thresholdValue;

    private String comparisonOperator;

    private String priority;

    private Boolean isActive;
}
