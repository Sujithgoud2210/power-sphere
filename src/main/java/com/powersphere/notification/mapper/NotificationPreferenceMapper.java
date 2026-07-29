package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.entity.NotificationPreference;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationPreferenceMapper {

    NotificationPreference toEntity(NotificationPreferenceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget NotificationPreference preference, NotificationPreferenceRequest request);

    NotificationPreferenceResponse toResponse(NotificationPreference preference);
}
