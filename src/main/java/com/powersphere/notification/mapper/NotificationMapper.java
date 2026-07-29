package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.entity.Notification;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    Notification toEntity(CreateNotificationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Notification notification, UpdateNotificationRequest request);

    NotificationResponse toResponse(Notification notification);
}
