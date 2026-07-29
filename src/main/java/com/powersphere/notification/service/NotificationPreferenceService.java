package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.NotificationPreferenceRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;

import java.util.List;

public interface NotificationPreferenceService {

    NotificationPreferenceResponse createPreference(NotificationPreferenceRequest request);

    NotificationPreferenceResponse updatePreference(Long id, NotificationPreferenceRequest request);

    void deletePreference(Long id);

    NotificationPreferenceResponse getPreferenceById(Long id);

    List<NotificationPreferenceResponse> getPreferencesByUserId(Long userId);

    NotificationPreferenceResponse togglePreference(Long id, boolean enabled);
}
