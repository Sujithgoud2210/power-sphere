package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.entity.NotificationPreference;
import com.powersphere.notification.exception.NotificationPreferenceNotFoundException;
import com.powersphere.notification.mapper.NotificationPreferenceMapper;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.repository.NotificationPreferenceRepository;
import com.powersphere.notification.service.impl.NotificationPreferenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationPreferenceServiceTest {

    @Mock
    private NotificationPreferenceRepository preferenceRepository;

    @Mock
    private NotificationPreferenceMapper preferenceMapper;

    private NotificationPreferenceService preferenceService;

    @BeforeEach
    void setUp() {
        preferenceService = new NotificationPreferenceServiceImpl(preferenceRepository, preferenceMapper);
    }

    @Test
    void createPreference_ShouldReturnResponse() {
        NotificationPreferenceRequest request = NotificationPreferenceRequest.builder()
                .userId(1L)
                .channel(NotificationChannel.EMAIL)
                .build();

        NotificationPreference preference = NotificationPreference.builder()
                .id(1L)
                .userId(1L)
                .channel(NotificationChannel.EMAIL)
                .isEnabled(true)
                .build();

        NotificationPreferenceResponse response = NotificationPreferenceResponse.builder()
                .id(1L)
                .userId(1L)
                .channel("EMAIL")
                .isEnabled(true)
                .build();

        when(preferenceMapper.toEntity(request)).thenReturn(preference);
        when(preferenceRepository.save(any(NotificationPreference.class))).thenReturn(preference);
        when(preferenceMapper.toResponse(preference)).thenReturn(response);

        NotificationPreferenceResponse result = preferenceService.createPreference(request);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("EMAIL", result.getChannel());
    }

    @Test
    void getPreferenceById_ShouldReturnResponse() {
        NotificationPreference preference = NotificationPreference.builder()
                .id(1L)
                .userId(1L)
                .build();

        NotificationPreferenceResponse response = NotificationPreferenceResponse.builder()
                .id(1L)
                .userId(1L)
                .build();

        when(preferenceRepository.findById(1L)).thenReturn(Optional.of(preference));
        when(preferenceMapper.toResponse(preference)).thenReturn(response);

        NotificationPreferenceResponse result = preferenceService.getPreferenceById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getPreferenceById_ShouldThrowException_WhenNotFound() {
        when(preferenceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotificationPreferenceNotFoundException.class,
                () -> preferenceService.getPreferenceById(999L));
    }

    @Test
    void deletePreference_ShouldDeleteSuccessfully() {
        NotificationPreference preference = NotificationPreference.builder().id(1L).build();
        when(preferenceRepository.findById(1L)).thenReturn(Optional.of(preference));

        preferenceService.deletePreference(1L);

        verify(preferenceRepository).delete(preference);
    }

    @Test
    void getPreferencesByUserId_ShouldReturnList() {
        NotificationPreference preference = NotificationPreference.builder()
                .id(1L).userId(1L).build();

        NotificationPreferenceResponse response = NotificationPreferenceResponse.builder()
                .id(1L).userId(1L).build();

        when(preferenceRepository.findByUserId(1L)).thenReturn(List.of(preference));
        when(preferenceMapper.toResponse(preference)).thenReturn(response);

        List<NotificationPreferenceResponse> results = preferenceService.getPreferencesByUserId(1L);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void togglePreference_ShouldUpdateEnabledStatus() {
        NotificationPreference preference = NotificationPreference.builder()
                .id(1L)
                .isEnabled(false)
                .build();

        NotificationPreferenceResponse response = NotificationPreferenceResponse.builder()
                .id(1L)
                .isEnabled(true)
                .build();

        when(preferenceRepository.findById(1L)).thenReturn(Optional.of(preference));
        when(preferenceRepository.save(any(NotificationPreference.class))).thenReturn(preference);
        when(preferenceMapper.toResponse(preference)).thenReturn(response);

        NotificationPreferenceResponse result = preferenceService.togglePreference(1L, true);

        assertNotNull(result);
        assertTrue(result.getIsEnabled());
    }
}
