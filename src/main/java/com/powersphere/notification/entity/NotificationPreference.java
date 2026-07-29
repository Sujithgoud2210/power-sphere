package com.powersphere.notification.entity;

import com.powersphere.notification.model.NotificationChannel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private NotificationChannel channel;

    @Column(name = "notify_on_bill", nullable = false)
    @Builder.Default
    private Boolean notifyOnBill = true;

    @Column(name = "notify_on_energy_alert", nullable = false)
    @Builder.Default
    private Boolean notifyOnEnergyAlert = true;

    @Column(name = "notify_on_meter_event", nullable = false)
    @Builder.Default
    private Boolean notifyOnMeterEvent = true;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (notifyOnBill == null) notifyOnBill = true;
        if (notifyOnEnergyAlert == null) notifyOnEnergyAlert = true;
        if (notifyOnMeterEvent == null) notifyOnMeterEvent = true;
        if (isEnabled == null) isEnabled = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
