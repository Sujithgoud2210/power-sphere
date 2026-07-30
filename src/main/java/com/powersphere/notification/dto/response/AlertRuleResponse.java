package com.powersphere.notification.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRuleResponse {

    private Long id;
    private String name;
    private String description;
    private String eventType;
    private String conditionExpression;
    private Double thresholdValue;
    private String comparisonOperator;
    private String priority;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
