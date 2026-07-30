package com.powersphere.energy.entity;

import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingSource;
import com.powersphere.energy.enums.ReadingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "energy_readings", indexes = {
        @Index(name = "idx_meter_id", columnList = "meterId"),
        @Index(name = "idx_reading_timestamp", columnList = "readingTimestamp"),
        @Index(name = "idx_meter_reading_time", columnList = "meterId, readingTimestamp", unique = true),
        @Index(name = "idx_active", columnList = "active"),
        @Index(name = "idx_reading_type", columnList = "readingType")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private Long meterId;

    @Column(nullable = false)
    @ToString.Include
    private LocalDateTime readingTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingType readingType;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal previousReading;

    @Column(nullable = false, precision = 12, scale = 3)
    @ToString.Include
    private BigDecimal currentReading;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal consumption;

    @Column(precision = 8, scale = 3)
    private BigDecimal voltage;

    @Column(precision = 8, scale = 3)
    private BigDecimal current;

    @Column(precision = 5, scale = 3)
    private BigDecimal powerFactor;

    @Column(precision = 6, scale = 3)
    private BigDecimal frequency;

    @Column(precision = 12, scale = 3)
    private BigDecimal power;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(precision = 5, scale = 2)
    private BigDecimal batteryLevel;

    @Column(precision = 3, scale = 1)
    private BigDecimal signalStrength;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingSource readingSource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private QualityStatus qualityStatus = QualityStatus.VALID;

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergyReading that = (EnergyReading) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
