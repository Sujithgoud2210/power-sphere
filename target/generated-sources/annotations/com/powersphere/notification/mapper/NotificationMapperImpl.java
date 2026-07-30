package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.entity.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(CreateNotificationRequest request) {
        if ( request == null ) {
            return null;
        }

        Notification.NotificationBuilder notification = Notification.builder();

        notification.title( request.getTitle() );
        notification.message( request.getMessage() );
        notification.recipientUser( request.getRecipientUser() );
        notification.recipientEmail( request.getRecipientEmail() );
        notification.recipientPhone( request.getRecipientPhone() );
        notification.notificationType( request.getNotificationType() );
        notification.priority( request.getPriority() );
        notification.scheduledTime( request.getScheduledTime() );
        notification.channel( request.getChannel() );
        notification.remarks( request.getRemarks() );
        notification.billId( request.getBillId() );
        notification.energyAlertId( request.getEnergyAlertId() );
        notification.meterEventId( request.getMeterEventId() );

        return notification.build();
    }

    @Override
    public void updateEntity(Notification notification, UpdateNotificationRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getTitle() != null ) {
            notification.setTitle( request.getTitle() );
        }
        if ( request.getMessage() != null ) {
            notification.setMessage( request.getMessage() );
        }
        if ( request.getRecipientUser() != null ) {
            notification.setRecipientUser( request.getRecipientUser() );
        }
        if ( request.getRecipientEmail() != null ) {
            notification.setRecipientEmail( request.getRecipientEmail() );
        }
        if ( request.getRecipientPhone() != null ) {
            notification.setRecipientPhone( request.getRecipientPhone() );
        }
        if ( request.getNotificationType() != null ) {
            notification.setNotificationType( request.getNotificationType() );
        }
        if ( request.getPriority() != null ) {
            notification.setPriority( request.getPriority() );
        }
        if ( request.getScheduledTime() != null ) {
            notification.setScheduledTime( request.getScheduledTime() );
        }
        if ( request.getChannel() != null ) {
            notification.setChannel( request.getChannel() );
        }
        if ( request.getRemarks() != null ) {
            notification.setRemarks( request.getRemarks() );
        }
        if ( request.getBillId() != null ) {
            notification.setBillId( request.getBillId() );
        }
        if ( request.getEnergyAlertId() != null ) {
            notification.setEnergyAlertId( request.getEnergyAlertId() );
        }
        if ( request.getMeterEventId() != null ) {
            notification.setMeterEventId( request.getMeterEventId() );
        }
    }

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponse.NotificationResponseBuilder notificationResponse = NotificationResponse.builder();

        notificationResponse.id( notification.getId() );
        notificationResponse.title( notification.getTitle() );
        notificationResponse.message( notification.getMessage() );
        notificationResponse.recipientUser( notification.getRecipientUser() );
        notificationResponse.recipientEmail( notification.getRecipientEmail() );
        notificationResponse.recipientPhone( notification.getRecipientPhone() );
        if ( notification.getNotificationType() != null ) {
            notificationResponse.notificationType( notification.getNotificationType().name() );
        }
        if ( notification.getPriority() != null ) {
            notificationResponse.priority( notification.getPriority().name() );
        }
        if ( notification.getStatus() != null ) {
            notificationResponse.status( notification.getStatus().name() );
        }
        notificationResponse.scheduledTime( notification.getScheduledTime() );
        notificationResponse.sentTime( notification.getSentTime() );
        notificationResponse.readTime( notification.getReadTime() );
        if ( notification.getChannel() != null ) {
            notificationResponse.channel( notification.getChannel().name() );
        }
        notificationResponse.retryCount( notification.getRetryCount() );
        notificationResponse.remarks( notification.getRemarks() );
        notificationResponse.billId( notification.getBillId() );
        notificationResponse.energyAlertId( notification.getEnergyAlertId() );
        notificationResponse.meterEventId( notification.getMeterEventId() );
        notificationResponse.createdAt( notification.getCreatedAt() );
        notificationResponse.updatedAt( notification.getUpdatedAt() );

        return notificationResponse.build();
    }
}
