package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.NotificationSearchRequest;
import com.powersphere.notification.dto.request.SendNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PageResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.entity.NotificationTemplate;
import com.powersphere.notification.enums.NotificationStatus;
import com.powersphere.notification.event.NotificationBatchEvent;
import com.powersphere.notification.event.NotificationEvent;
import com.powersphere.notification.event.NotificationStatusChangeEvent;
import com.powersphere.notification.exception.NotificationNotFoundException;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.repository.NotificationRepository;
import com.powersphere.notification.repository.NotificationTemplateRepository;
import com.powersphere.notification.service.NotificationService;
import com.powersphere.notification.util.NotificationTemplateEngine;
import com.powersphere.notification.validation.NotificationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link NotificationService} interface. Handles the
 * full lifecycle of notifications from creation through delivery tracking.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationMapper mapper;
    private final NotificationValidator validator;
    private final NotificationTemplateEngine templateEngine;
    private final ApplicationEventPublisher eventPublisher;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            NotificationTemplateRepository templateRepository,
            NotificationMapper mapper,
            NotificationValidator validator,
            NotificationTemplateEngine templateEngine,
            ApplicationEventPublisher eventPublisher) {
        this.notificationRepository = notificationRepository;
        this.templateRepository = templateRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.templateEngine = templateEngine;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public NotificationResponse sendNotification(SendNotificationRequest request) {
        // Validate request
        List<String> errors = validator.validate(request);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Validation failed: " + String.join("; ", errors));
        }

        // Resolve template if provided
        if (request.getTemplateCode() != null && !request.getTemplateCode().isBlank()) {
            resolveTemplate(request);
        }

        // Create notification entity
        Notification notification = mapper.toEntity(request);

        // Save notification
        notification = notificationRepository.save(notification);

        log.info("Notification created: id={}, type={}, channel={}, recipientId={}",
                notification.getId(), notification.getType(),
                notification.getChannel(), notification.getRecipientId());

        // Publish event for async processing
        eventPublisher.publishEvent(new NotificationEvent(this, notification));

        return mapper.toResponse(notification);
    }

    @Override
    public List<NotificationResponse> sendBatchNotifications(List<SendNotificationRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return Collections.emptyList();
        }

        // Validate all requests and resolve templates
        List<Notification> notifications = requests.stream()
                .peek(request -> {
                    List<String> errors = validator.validate(request);
                    if (!errors.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Validation failed: " + String.join("; ", errors));
                    }
                    if (request.getTemplateCode() != null && !request.getTemplateCode().isBlank()) {
                        resolveTemplate(request);
                    }
                })
                .map(mapper::toEntity)
                .collect(Collectors.toList());

        // Batch save
        notifications = notificationRepository.saveAll(notifications);

        log.info("Batch notification created: count={}", notifications.size());

        // Publish batch event
        eventPublisher.publishEvent(new NotificationBatchEvent(this, notifications));

        return mapper.toResponseList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        return mapper.toResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationForRecipient(Long notificationId, Long recipientId) {
        Notification notification = notificationRepository.findByIdAndRecipientId(notificationId, recipientId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification not found for id: " + notificationId + " and recipient: " + recipientId));
        return mapper.toResponse(notification);
    }

    @Override
    public NotificationResponse updateNotification(Long id, UpdateNotificationRequest request) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        NotificationStatus previousStatus = notification.getStatus();

        Optional.ofNullable(request.getStatus()).ifPresent(notification::setStatus);
        Optional.ofNullable(request.getPriority()).ifPresent(notification::setPriority);
        Optional.ofNullable(request.getSubject()).ifPresent(notification::setSubject);
        Optional.ofNullable(request.getContent()).ifPresent(notification::setContent);
        Optional.ofNullable(request.getScheduledFor()).ifPresent(notification::setScheduledFor);
        Optional.ofNullable(request.getMaxRetries()).ifPresent(notification::setMaxRetries);

        notification = notificationRepository.save(notification);

        // Publish status change event if status changed
        if (request.getStatus() != null && !request.getStatus().equals(previousStatus)) {
            eventPublisher.publishEvent(new NotificationStatusChangeEvent(
                    this, notification, previousStatus, notification.getStatus()));
        }

        return mapper.toResponse(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new NotificationNotFoundException(id);
        }
        notificationRepository.deleteById(id);
        log.info("Notification deleted: id={}", id);
    }

    @Override
    public NotificationResponse markAsRead(Long notificationId, Long recipientId) {
        Notification notification = notificationRepository.findByIdAndRecipientId(notificationId, recipientId)
                .orElseThrow(() -> new NotificationNotFoundException(
                        "Notification not found for id: " + notificationId + " and recipient: " + recipientId));

        NotificationStatus previousStatus = notification.getStatus();
        notification.markRead();
        notification = notificationRepository.save(notification);

        eventPublisher.publishEvent(new NotificationStatusChangeEvent(
                this, notification, previousStatus, notification.getStatus()));

        return mapper.toResponse(notification);
    }

    @Override
    public NotificationResponse archiveNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        NotificationStatus previousStatus = notification.getStatus();
        notification.archive();
        notification = notificationRepository.save(notification);

        eventPublisher.publishEvent(new NotificationStatusChangeEvent(
                this, notification, previousStatus, notification.getStatus()));

        return mapper.toResponse(notification);
    }

    @Override
    public int archiveAllRead(Long recipientId) {
        int archived = notificationRepository.archiveAllReadByRecipient(recipientId);
        if (archived > 0) {
            log.info("Archived {} read notifications for recipientId={}", archived, recipientId);
        }
        return archived;
    }

    @Override
    public NotificationResponse cancelNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        if (notification.cancel()) {
            notification = notificationRepository.save(notification);
            log.info("Notification cancelled: id={}", id);
        } else {
            throw new IllegalStateException(
                    "Cannot cancel notification " + id + " with status " + notification.getStatus());
        }

        return mapper.toResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> searchNotifications(NotificationSearchRequest request) {
        Sort sort = Sort.by(
                Sort.Direction.fromString(request.getSortDirection()),
                request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Notification> page = notificationRepository.searchNotifications(
                request.getRecipientId(),
                request.getSenderId(),
                request.getOrganizationId(),
                request.getType(),
                request.getStatus(),
                request.getPriority(),
                request.getChannel(),
                request.getDateFrom(),
                request.getDateTo(),
                request.getQuery(),
                pageable);

        return mapper.toPageResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<NotificationResponse> getNotificationsByRecipient(Long recipientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Notification> notificationPage = notificationRepository.findByRecipientId(recipientId, pageable);
        return mapper.toPageResponse(notificationPage);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long recipientId) {
        return notificationRepository.countByRecipientIdAndStatus(recipientId, NotificationStatus.DELIVERED);
    }

    /**
     * Resolves and renders a notification template for the given request.
     * Populates the subject and content from the template if not already set.
     */
    private void resolveTemplate(SendNotificationRequest request) {
        Optional<NotificationTemplate> templateOpt = templateRepository.findByCode(request.getTemplateCode());
        if (templateOpt.isEmpty()) {
            log.warn("Template not found for code: {}, proceeding with raw content", request.getTemplateCode());
            return;
        }

        NotificationTemplate template = templateOpt.get();
        Map<String, String> variables = Optional.ofNullable(request.getTemplateVariables())
                .orElse(Collections.emptyMap());

        NotificationTemplateEngine.RenderedContent rendered = templateEngine.renderTemplate(template, variables);

        if (request.getSubject() == null || request.getSubject().isBlank()) {
            request.setSubject(rendered.getSubject());
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            request.setContent(rendered.getBody());
        }
    }
}
