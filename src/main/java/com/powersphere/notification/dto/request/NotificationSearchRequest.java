package com.powersphere.notification.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationSearchRequest {

    private String title;

    private String status;

    private String priority;

    private String channel;

    private Long recipientUser;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    private String sortBy;

    private String sortDirection;
}
