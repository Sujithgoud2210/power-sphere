package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.entity.AlertRule;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class AlertRuleMapperImpl implements AlertRuleMapper {

    @Override
    public AlertRule toEntity(CreateAlertRuleRequest request) {
        if ( request == null ) {
            return null;
        }

        AlertRule.AlertRuleBuilder alertRule = AlertRule.builder();

        alertRule.name( request.getName() );
        alertRule.description( request.getDescription() );
        alertRule.eventType( request.getEventType() );
        alertRule.conditionExpression( request.getConditionExpression() );
        alertRule.thresholdValue( request.getThresholdValue() );
        alertRule.comparisonOperator( request.getComparisonOperator() );
        alertRule.isActive( request.getIsActive() );

        alertRule.priority( mapPriority(request.getPriority()) );

        return alertRule.build();
    }

    @Override
    public void updateEntity(AlertRule alertRule, UpdateAlertRuleRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            alertRule.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            alertRule.setDescription( request.getDescription() );
        }
        if ( request.getEventType() != null ) {
            alertRule.setEventType( request.getEventType() );
        }
        if ( request.getConditionExpression() != null ) {
            alertRule.setConditionExpression( request.getConditionExpression() );
        }
        if ( request.getThresholdValue() != null ) {
            alertRule.setThresholdValue( request.getThresholdValue() );
        }
        if ( request.getComparisonOperator() != null ) {
            alertRule.setComparisonOperator( request.getComparisonOperator() );
        }
        if ( request.getIsActive() != null ) {
            alertRule.setIsActive( request.getIsActive() );
        }

        alertRule.setPriority( mapPriority(request.getPriority()) );
    }

    @Override
    public AlertRuleResponse toResponse(AlertRule alertRule) {
        if ( alertRule == null ) {
            return null;
        }

        AlertRuleResponse.AlertRuleResponseBuilder alertRuleResponse = AlertRuleResponse.builder();

        alertRuleResponse.priority( priorityToString( alertRule.getPriority() ) );
        alertRuleResponse.id( alertRule.getId() );
        alertRuleResponse.name( alertRule.getName() );
        alertRuleResponse.description( alertRule.getDescription() );
        alertRuleResponse.eventType( alertRule.getEventType() );
        alertRuleResponse.conditionExpression( alertRule.getConditionExpression() );
        alertRuleResponse.thresholdValue( alertRule.getThresholdValue() );
        alertRuleResponse.comparisonOperator( alertRule.getComparisonOperator() );
        alertRuleResponse.isActive( alertRule.getIsActive() );
        alertRuleResponse.createdAt( alertRule.getCreatedAt() );
        alertRuleResponse.updatedAt( alertRule.getUpdatedAt() );

        return alertRuleResponse.build();
    }
}
