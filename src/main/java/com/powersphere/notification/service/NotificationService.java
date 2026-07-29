package com.powersphere.notification.service;

import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.NotificationSearchRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    NotificationResponse createNotification(CreateNotificationRequest request);

    NotificationResponse updateNotification(Long id, UpdateNotificationRequest request);

    void deleteNotification(Long id);

    NotificationResponse getNotificationById(Long id);

    PagedResponse<NotificationResponse> getAllNotifications(Pageable pageable);

    NotificationResponse markAsRead(Long id);

    NotificationResponse cancelNotification(Long id);

    PagedResponse<NotificationResponse> searchNotifications(NotificationSearchRequest searchRequest);
}
