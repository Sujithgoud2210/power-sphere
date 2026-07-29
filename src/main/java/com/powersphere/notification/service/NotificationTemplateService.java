package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.NotificationTemplateRequest;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.enums.NotificationChannel;
import com.powersphere.notification.enums.NotificationType;

import java.util.List;

/**
 * Service interface for managing notification templates. Templates define reusable
 * notification content structures with variable support for dynamic content rendering.
 */
public interface NotificationTemplateService {

    /**
     * Creates a new notification template.
     *
     * @param request the template creation request
     * @return the created template response
     */
    NotificationTemplateResponse createTemplate(NotificationTemplateRequest request);

    /**
     * Updates an existing notification template.
     *
     * @param id      the template ID to update
     * @param request the update request
     * @return the updated template response
     */
    NotificationTemplateResponse updateTemplate(Long id, NotificationTemplateRequest request);

    /**
     * Retrieves a template by its ID.
     *
     * @param id the template ID
     * @return the template response
     */
    NotificationTemplateResponse getTemplate(Long id);

    /**
     * Retrieves a template by its unique code.
     *
     * @param code the template code
     * @return the template response
     */
    NotificationTemplateResponse getTemplateByCode(String code);

    /**
     * Deletes a template by its ID.
     *
     * @param id the template ID to delete
     */
    void deleteTemplate(Long id);

    /**
     * Retrieves all templates matching the given type and channel.
     *
     * @param type    the notification type
     * @param channel the notification channel
     * @return list of matching template responses
     */
    List<NotificationTemplateResponse> getTemplatesByTypeAndChannel(NotificationType type, NotificationChannel channel);

    /**
     * Retrieves all active templates.
     *
     * @return list of all active template responses
     */
    List<NotificationTemplateResponse> getAllActiveTemplates();
}
