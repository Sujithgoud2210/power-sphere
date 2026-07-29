package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.entity.NotificationPreference;
import com.powersphere.notification.model.NotificationChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationPreferenceMapperTest {

    private final NotificationPreferenceMapper mapper = Mappers.getMapper(NotificationPreferenceMapper.class);

    @Test
    void toEntity_ShouldMapAllFields() {
        NotificationPreferenceRequest request = NotificationPreferenceRequest.builder()
                .userId(1L)
                .channel(NotificationChannel.EMAIL)
                .notifyOnBill(true)
                .notifyOnEnergyAlert(false)
                .notifyOnMeterEvent(true)
                .isEnabled(true)
                .build();

        NotificationPreference entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals(1L, entity.getUserId());
        assertEquals(NotificationChannel.EMAIL, entity.getChannel());
        assertTrue(entity.getNotifyOnBill());
        assertFalse(entity.getNotifyOnEnergyAlert());
        assertTrue(entity.getNotifyOnMeterEvent());
        assertTrue(entity.getIsEnabled());
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        NotificationPreference entity = NotificationPreference.builder()
                .id(1L)
                .userId(1L)
                .channel(NotificationChannel.SMS)
                .notifyOnBill(true)
                .notifyOnEnergyAlert(true)
                .notifyOnMeterEvent(false)
                .isEnabled(true)
                .createdAt(LocalDateTime.of(2026, 7, 29, 10, 0))
                .updatedAt(LocalDateTime.of(2026, 7, 29, 11, 0))
                .build();

        NotificationPreferenceResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("SMS", response.getChannel());
        assertTrue(response.getNotifyOnBill());
        assertTrue(response.getNotifyOnEnergyAlert());
        assertFalse(response.getNotifyOnMeterEvent());
        assertTrue(response.getIsEnabled());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }
}
