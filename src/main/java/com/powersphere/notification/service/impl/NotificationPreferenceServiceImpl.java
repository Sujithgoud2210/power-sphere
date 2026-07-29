package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.entity.NotificationPreference;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.repository.NotificationPreferenceRepository;
import com.powersphere.notification.service.NotificationPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link NotificationPreferenceService} interface.
 * Manages user-specific notification preferences including channel enablement,
 * quiet hours, and notification type suppression.
 */
@Service
@Transactional
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private static final Logger log = LoggerFactory.getLogger(NotificationPreferenceServiceImpl.class);

    private final NotificationPreferenceRepository preferenceRepository;
    private final NotificationMapper mapper;

    public NotificationPreferenceServiceImpl(
            NotificationPreferenceRepository preferenceRepository,
            NotificationMapper mapper) {
        this.preferenceRepository = preferenceRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationPreferenceResponse getPreferences(Long userId) {
        NotificationPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
        return mapper.toPreferenceResponse(preference);
    }

    @Override
    public NotificationPreferenceResponse updatePreferences(Long userId, NotificationPreferenceRequest request) {
        NotificationPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> {
                    NotificationPreference newPref = NotificationPreference.builder()
                            .userId(userId)
                            .build();
                    return preferenceRepository.save(newPref);
                });

        mapper.updatePreferenceFromRequest(preference, request);
        preference = preferenceRepository.save(preference);

        log.info("Notification preferences updated for userId={}", userId);

        return mapper.toPreferenceResponse(preference);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isChannelEnabled(Long userId, String channel) {
        NotificationPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
        return preference.isChannelEnabled(
                com.powersphere.notification.enums.NotificationChannel.valueOf(channel.toUpperCase()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNotificationTypeEnabled(Long userId, String type) {
        NotificationPreference preference = preferenceRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultPreferences(userId));
        return preference.isTypeEnabled(
                com.powersphere.notification.enums.NotificationType.valueOf(type.toUpperCase()));
    }

    /**
     * Creates default notification preferences for a user.
     */
    private NotificationPreference createDefaultPreferences(Long userId) {
        NotificationPreference preference = NotificationPreference.builder()
                .userId(userId)
                .build();
        preference = preferenceRepository.save(preference);
        log.debug("Default notification preferences created for userId={}", userId);
        return preference;
    }
}
