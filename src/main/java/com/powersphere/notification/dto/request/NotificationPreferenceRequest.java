package com.powersphere.notification.dto.request;

import com.powersphere.notification.model.NotificationChannel;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreferenceRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    private Boolean notifyOnBill;

    private Boolean notifyOnEnergyAlert;

    private Boolean notifyOnMeterEvent;

    private Boolean isEnabled;
}
