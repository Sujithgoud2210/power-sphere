package com.powersphere.meter.entity;

import com.powersphere.authentication.entity.User;
import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "smart_meters")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmartMeter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "meter_number", unique = true, nullable = false, length = 50)
    private String meterNumber;

    @Column(name = "serial_number", unique = true, nullable = false, length = 100)
    private String serialNumber;

    @Column(nullable = false, length = 100)
    private String manufacturer;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "firmware_version", length = 50)
    private String firmwareVersion;

    @Column(name = "installation_date")
    private LocalDateTime installationDate;

    @Column(name = "activation_date")
    private LocalDateTime activationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MeterStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "meter_type", nullable = false, length = 20)
    private MeterType meterType;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase_type", length = 20)
    private PhaseType phaseType;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type", nullable = false, length = 20)
    private ConnectionType connectionType;

    @Column(precision = 10, scale = 2)
    private BigDecimal voltage;

    @Column(name = "current_rating", precision = 10, scale = 2)
    private BigDecimal currentRating;

    @Column(name = "max_load", precision = 10, scale = 2)
    private BigDecimal maxLoad;

    @Column(precision = 12, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 12, scale = 8)
    private BigDecimal longitude;

    @Column(name = "installation_address", columnDefinition = "TEXT")
    private String installationAddress;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    @Column(length = 500)
    private String barcode;

    @Column(name = "last_communication_time")
    private LocalDateTime lastCommunicationTime;

    @Column(name = "last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(updatable = false)
    private String createdBy;

    private String updatedBy;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = MeterStatus.INACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
