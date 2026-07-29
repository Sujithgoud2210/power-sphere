package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.entity.NotificationPreference;
import com.powersphere.notification.exception.NotificationPreferenceNotFoundException;
import com.powersphere.notification.mapper.NotificationPreferenceMapper;
import com.powersphere.notification.repository.NotificationPreferenceRepository;
import com.powersphere.notification.service.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final NotificationPreferenceMapper preferenceMapper;

    @Override
    public NotificationPreferenceResponse createPreference(NotificationPreferenceRequest request) {
        NotificationPreference preference = preferenceMapper.toEntity(request);
        NotificationPreference saved = preferenceRepository.save(preference);
        log.info("Notification preference created: id={}, userId={}, channel={}",
                saved.getId(), saved.getUserId(), saved.getChannel());
        return preferenceMapper.toResponse(saved);
    }

    @Override
    public NotificationPreferenceResponse updatePreference(Long id, NotificationPreferenceRequest request) {
        NotificationPreference preference = findPreferenceOrThrow(id);
        preferenceMapper.updateEntity(preference, request);
        NotificationPreference updated = preferenceRepository.save(preference);
        log.info("Notification preference updated: id={}", updated.getId());
        return preferenceMapper.toResponse(updated);
    }

    @Override
    public void deletePreference(Long id) {
        NotificationPreference preference = findPreferenceOrThrow(id);
        preferenceRepository.delete(preference);
        log.info("Notification preference deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationPreferenceResponse getPreferenceById(Long id) {
        return preferenceMapper.toResponse(findPreferenceOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationPreferenceResponse> getPreferencesByUserId(Long userId) {
        return preferenceRepository.findByUserId(userId)
                .stream()
                .map(preferenceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationPreferenceResponse togglePreference(Long id, boolean enabled) {
        NotificationPreference preference = findPreferenceOrThrow(id);
        preference.setIsEnabled(enabled);
        NotificationPreference saved = preferenceRepository.save(preference);
        log.info("Notification preference toggled: id={}, enabled={}", id, enabled);
        return preferenceMapper.toResponse(saved);
    }

    private NotificationPreference findPreferenceOrThrow(Long id) {
        return preferenceRepository.findById(id)
                .orElseThrow(() -> new NotificationPreferenceNotFoundException(id));
    }
}
