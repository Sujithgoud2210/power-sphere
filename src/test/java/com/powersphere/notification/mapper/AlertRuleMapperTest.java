package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.entity.AlertRule;
import com.powersphere.notification.model.NotificationPriority;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AlertRuleMapperTest {

    private final AlertRuleMapper mapper = Mappers.getMapper(AlertRuleMapper.class);

    @Test
    void toEntity_ShouldMapAllFields() {
        CreateAlertRuleRequest request = CreateAlertRuleRequest.builder()
                .name("High Usage Alert")
                .description("Alert when usage is high")
                .eventType("ENERGY_CONSUMPTION")
                .conditionExpression("value > threshold")
                .thresholdValue(1000.0)
                .comparisonOperator(">")
                .priority("HIGH")
                .isActive(true)
                .build();

        AlertRule entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("High Usage Alert", entity.getName());
        assertEquals("Alert when usage is high", entity.getDescription());
        assertEquals("ENERGY_CONSUMPTION", entity.getEventType());
        assertEquals("value > threshold", entity.getConditionExpression());
        assertEquals(1000.0, entity.getThresholdValue());
        assertEquals(">", entity.getComparisonOperator());
        assertEquals(NotificationPriority.HIGH, entity.getPriority());
        assertTrue(entity.getIsActive());
    }

    @Test
    void toEntity_ShouldDefaultPriority_WhenNull() {
        CreateAlertRuleRequest request = CreateAlertRuleRequest.builder()
                .name("Test Alert")
                .eventType("TEST")
                .build();

        AlertRule entity = mapper.toEntity(request);

        assertEquals(NotificationPriority.MEDIUM, entity.getPriority());
    }

    @Test
    void toEntity_ShouldDefaultPriority_WhenInvalid() {
        CreateAlertRuleRequest request = CreateAlertRuleRequest.builder()
                .name("Test Alert")
                .eventType("TEST")
                .priority("INVALID_PRIORITY")
                .build();

        AlertRule entity = mapper.toEntity(request);

        assertEquals(NotificationPriority.MEDIUM, entity.getPriority());
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        AlertRule entity = AlertRule.builder()
                .id(1L)
                .name("Critical Alert")
                .description("Critical energy alert")
                .eventType("ENERGY_OVERLOAD")
                .conditionExpression("load > 5000")
                .thresholdValue(5000.0)
                .comparisonOperator(">")
                .priority(NotificationPriority.CRITICAL)
                .isActive(true)
                .createdAt(LocalDateTime.of(2026, 7, 29, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 7, 29, 11, 0))
                .build();

        AlertRuleResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Critical Alert", response.getName());
        assertEquals("Critical energy alert", response.getDescription());
        assertEquals("ENERGY_OVERLOAD", response.getEventType());
        assertEquals("load > 5000", response.getConditionExpression());
        assertEquals(5000.0, response.getThresholdValue());
        assertEquals(">", response.getComparisonOperator());
        assertEquals("CRITICAL", response.getPriority());
        assertTrue(response.getIsActive());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }

    @Test
    void updateEntity_ShouldUpdateNonNullFields() {
        AlertRule entity = AlertRule.builder()
                .id(1L)
                .name("Original")
                .priority(NotificationPriority.LOW)
                .isActive(true)
                .build();

        UpdateAlertRuleRequest request = UpdateAlertRuleRequest.builder()
                .name("Updated")
                .priority("HIGH")
                .build();

        mapper.updateEntity(entity, request);

        assertEquals("Updated", entity.getName());
        assertEquals(NotificationPriority.HIGH, entity.getPriority());
        assertTrue(entity.getIsActive());
    }
}
