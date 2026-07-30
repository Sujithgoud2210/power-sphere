package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.CreateNotificationTemplateRequest;
import com.powersphere.notification.dto.request.UpdateNotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;

import java.util.List;

public interface NotificationTemplateService {

    NotificationTemplateResponse createTemplate(CreateNotificationTemplateRequest request);

    NotificationTemplateResponse updateTemplate(Long id, UpdateNotificationTemplateRequest request);

    void deleteTemplate(Long id);

    NotificationTemplateResponse getTemplateById(Long id);

    NotificationTemplateResponse getTemplateByCode(String code);

    List<NotificationTemplateResponse> getAllTemplates();
}
