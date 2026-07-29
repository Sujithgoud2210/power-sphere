package com.powersphere.notification.service.impl;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.NotificationSearchRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.entity.Notification;
import com.powersphere.notification.event.NotificationCreatedEvent;
import com.powersphere.notification.event.NotificationReadEvent;
import com.powersphere.notification.event.NotificationSentEvent;
import com.powersphere.notification.exception.NotificationNotFoundException;
import com.powersphere.notification.mapper.NotificationMapper;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.model.NotificationStatus;
import com.powersphere.notification.repository.NotificationRepository;
import com.powersphere.notification.service.NotificationService;
import com.powersphere.notification.validation.NotificationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationValidator notificationValidator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        notificationValidator.validateCreate(request);

        Notification notification = notificationMapper.toEntity(request);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setRetryCount(0);

        if (request.getScheduledTime() != null) {
            notificationValidator.validateScheduledNotification(request);
        }

        Notification saved = notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationCreatedEvent(this, saved));

        log.info("Notification created successfully: id={}, title='{}'", saved.getId(), saved.getTitle());
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = findNotificationOrThrow(id);
        return notificationMapper.toResponse(notification);
    }

    @Override
    public NotificationResponse updateNotification(Long id, UpdateNotificationRequest request) {
        notificationValidator.validateUpdate(request);

        Notification notification = findNotificationOrThrow(id);
        notificationMapper.updateEntity(notification, request);
        Notification updated = notificationRepository.save(notification);

        log.info("Notification updated: id={}", updated.getId());
        return notificationMapper.toResponse(updated);
    }

    @Override
    public void deleteNotification(Long id) {
        Notification notification = findNotificationOrThrow(id);
        notificationRepository.delete(notification);
        log.info("Notification deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<NotificationResponse> getAllNotifications(Pageable pageable) {
        Page<Notification> notificationPage = notificationRepository.findAll(pageable);
        return buildPagedResponse(notificationPage, pageable);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        Notification notification = findNotificationOrThrow(id);

        if (notification.getStatus() == NotificationStatus.READ) {
            throw new IllegalStateException("Notification is already marked as read");
        }

        notification.setStatus(NotificationStatus.READ);
        notification.setReadTime(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);

        eventPublisher.publishEvent(new NotificationReadEvent(this, saved));
        log.info("Notification marked as read: id={}", saved.getId());
        return notificationMapper.toResponse(saved);
    }

    @Override
    public NotificationResponse cancelNotification(Long id) {
        Notification notification = findNotificationOrThrow(id);

        if (notification.getStatus() == NotificationStatus.SENT
                || notification.getStatus() == NotificationStatus.READ) {
            throw new IllegalStateException(
                    "Cannot cancel notification with status: " + notification.getStatus());
        }

        notification.setStatus(NotificationStatus.FAILED);
        notification.setRemarks("Notification cancelled by user");
        Notification saved = notificationRepository.save(notification);

        log.info("Notification cancelled: id={}", saved.getId());
        return notificationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<NotificationResponse> searchNotifications(NotificationSearchRequest searchRequest) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
            Sort.Direction direction = Sort.Direction.DESC;
            if ("asc".equalsIgnoreCase(searchRequest.getSortDirection())) {
                direction = Sort.Direction.ASC;
            }
            sort = Sort.by(direction, searchRequest.getSortBy());
        }

        int page = Math.max(searchRequest.getPage(), 0);
        int size = searchRequest.getSize() > 0 ? searchRequest.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size, sort);

        NotificationStatus status = null;
        if (searchRequest.getStatus() != null && !searchRequest.getStatus().isEmpty()) {
            status = NotificationStatus.valueOf(searchRequest.getStatus().toUpperCase());
        }

        NotificationPriority priority = null;
        if (searchRequest.getPriority() != null && !searchRequest.getPriority().isEmpty()) {
            priority = NotificationPriority.valueOf(searchRequest.getPriority().toUpperCase());
        }

        NotificationChannel channel = null;
        if (searchRequest.getChannel() != null && !searchRequest.getChannel().isEmpty()) {
            channel = NotificationChannel.valueOf(searchRequest.getChannel().toUpperCase());
        }

        Page<Notification> notificationPage = notificationRepository.searchNotifications(
                searchRequest.getTitle(),
                status,
                priority,
                channel,
                searchRequest.getRecipientUser(),
                searchRequest.getStartDate(),
                searchRequest.getEndDate(),
                pageable
        );

        return buildPagedResponse(notificationPage, pageable);
    }

    private Notification findNotificationOrThrow(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    private PagedResponse<NotificationResponse> buildPagedResponse(Page<Notification> page, Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().stream().findFirst().orElse(null);
        return PagedResponse.<NotificationResponse>builder()
                .content(page.getContent().stream()
                        .map(notificationMapper::toResponse)
                        .collect(Collectors.toList()))
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .sortBy(sortOrder != null ? sortOrder.getProperty() : null)
                .sortDirection(sortOrder != null ? sortOrder.getDirection().name().toLowerCase() : null)
                .build();
    }
}
