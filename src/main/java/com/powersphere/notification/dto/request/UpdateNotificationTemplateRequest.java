package com.powersphere.notification.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateNotificationTemplateRequest {

    private String name;

    private String subject;

    private String body;

    private String description;

    private Boolean isActive;
}
