package com.powersphere.notification.mapper;

import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.dto.response.NotificationPreferenceResponse;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.NotificationTemplateResponse;
import com.powersphere.notification.dto.response.PageResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.entity.NotificationPreference;
import com.powersphere.notification.entity.NotificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Mapper component responsible for converting between notification entities
 * and their corresponding DTOs. Provides consistent mapping logic used
 * across the notification module.
 */
@Component
public class NotificationMapper {

    // --- Notification Mapping ---

    /**
     * Converts a {@link SendNotificationRequest} into a new {@link Notification} entity.
     */
    public Notification toEntity(SendNotificationRequest request) {
        return Notification.builder()
                .type(request.getType())
                .channel(request.getChannel())
                .priority(Optional.ofNullable(request.getPriority()).orElse(com.powersphere.notification.enums.NotificationPriority.MEDIUM))
                .subject(request.getSubject())
                .content(request.getContent())
                .templateCode(request.getTemplateCode())
                .senderId(request.getSenderId())
                .senderEmail(request.getSenderEmail())
                .senderName(request.getSenderName())
                .recipientId(request.getRecipientId())
                .recipientEmail(request.getRecipientEmail())
                .recipientPhone(request.getRecipientPhone())
                .recipientName(request.getRecipientName())
                .metadata(Optional.ofNullable(request.getMetadata()).orElse(Collections.emptyMap()))
                .attachments(Optional.ofNullable(request.getAttachments()).orElse(Collections.emptyList()))
                .maxRetries(Math.max(request.getMaxRetries(), 0))
                .scheduledFor(request.getScheduledFor())
                .organizationId(request.getOrganizationId())
                .build();
    }

    /**
     * Converts a {@link Notification} entity into a {@link NotificationResponse} DTO.
     */
    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setType(notification.getType());
        response.setStatus(notification.getStatus());
        response.setPriority(notification.getPriority());
        response.setChannel(notification.getChannel());
        response.setSubject(notification.getSubject());
        response.setContent(notification.getContent());
        response.setTemplateCode(notification.getTemplateCode());
        response.setSenderId(notification.getSenderId());
        response.setSenderEmail(notification.getSenderEmail());
        response.setSenderName(notification.getSenderName());
        response.setRecipientId(notification.getRecipientId());
        response.setRecipientEmail(notification.getRecipientEmail());
        response.setRecipientPhone(notification.getRecipientPhone());
        response.setRecipientName(notification.getRecipientName());
        response.setMetadata(notification.getMetadata());
        response.setAttachments(notification.getAttachments());
        response.setSentAt(notification.getSentAt());
        response.setDeliveredAt(notification.getDeliveredAt());
        response.setReadAt(notification.getReadAt());
        response.setErrorMessage(notification.getErrorMessage());
        response.setRetryCount(notification.getRetryCount());
        response.setMaxRetries(notification.getMaxRetries());
        response.setScheduledFor(notification.getScheduledFor());
        response.setOrganizationId(notification.getOrganizationId());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        return response;
    }

    /**
     * Converts a list of {@link Notification} entities into a list of {@link NotificationResponse} DTOs.
     */
    public List<NotificationResponse> toResponseList(List<Notification> notifications) {
        if (notifications == null) {
            return Collections.emptyList();
        }
        return notifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Spring Data {@link Page} of {@link Notification} entities into a {@link PageResponse}
     * of {@link NotificationResponse} DTOs.
     */
    public PageResponse<NotificationResponse> toPageResponse(Page<Notification> page) {
        List<NotificationResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    // --- Template Mapping ---

    /**
     * Converts a {@link NotificationTemplate} entity into a {@link NotificationTemplateResponse} DTO.
     */
    public NotificationTemplateResponse toTemplateResponse(NotificationTemplate template) {
        if (template == null) {
            return null;
        }
        NotificationTemplateResponse response = new NotificationTemplateResponse();
        response.setId(template.getId());
        response.setCode(template.getCode());
        response.setName(template.getName());
        response.setDescription(template.getDescription());
        response.setType(template.getType());
        response.setChannel(template.getChannel());
        response.setSubjectTemplate(template.getSubjectTemplate());
        response.setBodyTemplate(template.getBodyTemplate());
        response.setActive(template.isActive());
        response.setOrganizationId(template.getOrganizationId());
        response.setCreatedBy(template.getCreatedBy());
        response.setCreatedAt(template.getCreatedAt());
        response.setUpdatedAt(template.getUpdatedAt());
        return response;
    }

    /**
     * Converts a list of {@link NotificationTemplate} entities into a list of
     * {@link NotificationTemplateResponse} DTOs.
     */
    public List<NotificationTemplateResponse> toTemplateResponseList(List<NotificationTemplate> templates) {
        if (templates == null) {
            return Collections.emptyList();
        }
        return templates.stream()
                .map(this::toTemplateResponse)
                .collect(Collectors.toList());
    }

    // --- Preference Mapping ---

    /**
     * Converts a {@link NotificationPreference} entity into a {@link NotificationPreferenceResponse} DTO.
     */
    public NotificationPreferenceResponse toPreferenceResponse(NotificationPreference preference) {
        if (preference == null) {
            return null;
        }
        NotificationPreferenceResponse response = new NotificationPreferenceResponse();
        response.setId(preference.getId());
        response.setUserId(preference.getUserId());
        response.setOrganizationId(preference.getOrganizationId());
        response.setEmailEnabled(preference.isEmailEnabled());
        response.setSmsEnabled(preference.isSmsEnabled());
        response.setPushEnabled(preference.isPushEnabled());
        response.setInAppEnabled(preference.isInAppEnabled());
        response.setDigestFrequency(preference.getDigestFrequency());
        response.setQuietHoursStart(preference.getQuietHoursStart());
        response.setQuietHoursEnd(preference.getQuietHoursEnd());
        response.setQuietHoursEnabled(preference.isQuietHoursEnabled());
        response.setDisabledNotificationTypes(preference.getDisabledNotificationTypes());
        response.setCreatedAt(preference.getCreatedAt());
        response.setUpdatedAt(preference.getUpdatedAt());
        return response;
    }

    /**
     * Applies fields from a {@link com.powersphere.notification.dto.request.NotificationPreferenceRequest}
     * to an existing {@link NotificationPreference} entity.
     */
    public void updatePreferenceFromRequest(
            NotificationPreference preference,
            com.powersphere.notification.dto.request.NotificationPreferenceRequest request) {
        preference.setEmailEnabled(request.isEmailEnabled());
        preference.setSmsEnabled(request.isSmsEnabled());
        preference.setPushEnabled(request.isPushEnabled());
        preference.setInAppEnabled(request.isInAppEnabled());
        preference.setDigestFrequency(request.getDigestFrequency());
        preference.setQuietHoursStart(request.getQuietHoursStart());
        preference.setQuietHoursEnd(request.getQuietHoursEnd());
        preference.setQuietHoursEnabled(request.isQuietHoursEnabled());
        preference.setDisabledNotificationTypes(
                Optional.ofNullable(request.getDisabledNotificationTypes())
                        .orElse(Collections.emptySet()));
    }
}
