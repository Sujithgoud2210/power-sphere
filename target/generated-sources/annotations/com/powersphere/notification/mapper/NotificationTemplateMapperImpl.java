package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateNotificationTemplateRequest;
import com.powersphere.notification.dto.request.UpdateNotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.entity.NotificationTemplate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:05+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class NotificationTemplateMapperImpl implements NotificationTemplateMapper {

    @Override
    public NotificationTemplate toEntity(CreateNotificationTemplateRequest request) {
        if ( request == null ) {
            return null;
        }

        NotificationTemplate.NotificationTemplateBuilder notificationTemplate = NotificationTemplate.builder();

        notificationTemplate.code( request.getCode() );
        notificationTemplate.name( request.getName() );
        notificationTemplate.subject( request.getSubject() );
        notificationTemplate.body( request.getBody() );
        notificationTemplate.description( request.getDescription() );
        notificationTemplate.isActive( request.getIsActive() );

        return notificationTemplate.build();
    }

    @Override
    public void updateEntity(NotificationTemplate template, UpdateNotificationTemplateRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            template.setName( request.getName() );
        }
        if ( request.getSubject() != null ) {
            template.setSubject( request.getSubject() );
        }
        if ( request.getBody() != null ) {
            template.setBody( request.getBody() );
        }
        if ( request.getDescription() != null ) {
            template.setDescription( request.getDescription() );
        }
        if ( request.getIsActive() != null ) {
            template.setIsActive( request.getIsActive() );
        }
    }

    @Override
    public NotificationTemplateResponse toResponse(NotificationTemplate template) {
        if ( template == null ) {
            return null;
        }

        NotificationTemplateResponse.NotificationTemplateResponseBuilder notificationTemplateResponse = NotificationTemplateResponse.builder();

        notificationTemplateResponse.id( template.getId() );
        notificationTemplateResponse.code( template.getCode() );
        notificationTemplateResponse.name( template.getName() );
        notificationTemplateResponse.subject( template.getSubject() );
        notificationTemplateResponse.body( template.getBody() );
        notificationTemplateResponse.description( template.getDescription() );
        notificationTemplateResponse.isActive( template.getIsActive() );
        notificationTemplateResponse.createdAt( template.getCreatedAt() );
        notificationTemplateResponse.updatedAt( template.getUpdatedAt() );

        return notificationTemplateResponse.build();
    }
}
