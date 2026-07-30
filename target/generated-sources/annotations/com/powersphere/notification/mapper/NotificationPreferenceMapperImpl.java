package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.entity.NotificationPreference;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class NotificationPreferenceMapperImpl implements NotificationPreferenceMapper {

    @Override
    public NotificationPreference toEntity(NotificationPreferenceRequest request) {
        if ( request == null ) {
            return null;
        }

        NotificationPreference.NotificationPreferenceBuilder notificationPreference = NotificationPreference.builder();

        notificationPreference.userId( request.getUserId() );
        notificationPreference.channel( request.getChannel() );
        notificationPreference.notifyOnBill( request.getNotifyOnBill() );
        notificationPreference.notifyOnEnergyAlert( request.getNotifyOnEnergyAlert() );
        notificationPreference.notifyOnMeterEvent( request.getNotifyOnMeterEvent() );
        notificationPreference.isEnabled( request.getIsEnabled() );

        return notificationPreference.build();
    }

    @Override
    public void updateEntity(NotificationPreference preference, NotificationPreferenceRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getUserId() != null ) {
            preference.setUserId( request.getUserId() );
        }
        if ( request.getChannel() != null ) {
            preference.setChannel( request.getChannel() );
        }
        if ( request.getNotifyOnBill() != null ) {
            preference.setNotifyOnBill( request.getNotifyOnBill() );
        }
        if ( request.getNotifyOnEnergyAlert() != null ) {
            preference.setNotifyOnEnergyAlert( request.getNotifyOnEnergyAlert() );
        }
        if ( request.getNotifyOnMeterEvent() != null ) {
            preference.setNotifyOnMeterEvent( request.getNotifyOnMeterEvent() );
        }
        if ( request.getIsEnabled() != null ) {
            preference.setIsEnabled( request.getIsEnabled() );
        }
    }

    @Override
    public NotificationPreferenceResponse toResponse(NotificationPreference preference) {
        if ( preference == null ) {
            return null;
        }

        NotificationPreferenceResponse.NotificationPreferenceResponseBuilder notificationPreferenceResponse = NotificationPreferenceResponse.builder();

        notificationPreferenceResponse.id( preference.getId() );
        notificationPreferenceResponse.userId( preference.getUserId() );
        if ( preference.getChannel() != null ) {
            notificationPreferenceResponse.channel( preference.getChannel().name() );
        }
        notificationPreferenceResponse.notifyOnBill( preference.getNotifyOnBill() );
        notificationPreferenceResponse.notifyOnEnergyAlert( preference.getNotifyOnEnergyAlert() );
        notificationPreferenceResponse.notifyOnMeterEvent( preference.getNotifyOnMeterEvent() );
        notificationPreferenceResponse.isEnabled( preference.getIsEnabled() );
        notificationPreferenceResponse.createdAt( preference.getCreatedAt() );
        notificationPreferenceResponse.updatedAt( preference.getUpdatedAt() );

        return notificationPreferenceResponse.build();
    }
}
