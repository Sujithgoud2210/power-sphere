package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateAlertRuleRequest;
import com.powersphere.notification.dto.request.UpdateAlertRuleRequest;
import com.powersphere.notification.dto.response.AlertRuleResponse;
import com.powersphere.notification.entity.AlertRule;
import com.powersphere.notification.model.NotificationPriority;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AlertRuleMapper {

    @Mapping(target = "priority", expression = "java(mapPriority(request.getPriority()))")
    AlertRule toEntity(CreateAlertRuleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "priority", expression = "java(mapPriority(request.getPriority()))")
    void updateEntity(@MappingTarget AlertRule alertRule, UpdateAlertRuleRequest request);

    @Mapping(target = "priority", source = "priority", qualifiedByName = "priorityToString")
    AlertRuleResponse toResponse(AlertRule alertRule);

    default NotificationPriority mapPriority(String priority) {
        if (priority == null || priority.isEmpty()) {
            return NotificationPriority.MEDIUM;
        }
        try {
            return NotificationPriority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NotificationPriority.MEDIUM;
        }
    }

    @Named("priorityToString")
    default String priorityToString(NotificationPriority priority) {
        return priority != null ? priority.name() : null;
    }
}
