package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateNotificationTemplateRequest;
import com.powersphere.notification.dto.request.UpdateNotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.entity.NotificationTemplate;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationTemplateMapper {

    NotificationTemplate toEntity(CreateNotificationTemplateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget NotificationTemplate template, UpdateNotificationTemplateRequest request);

    NotificationTemplateResponse toResponse(NotificationTemplate template);
}
