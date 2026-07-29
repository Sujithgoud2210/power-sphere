package com.powersphere.notification.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferenceResponse {

    private Long id;
    private Long userId;
    private String channel;
    private Boolean notifyOnBill;
    private Boolean notifyOnEnergyAlert;
    private Boolean notifyOnMeterEvent;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
