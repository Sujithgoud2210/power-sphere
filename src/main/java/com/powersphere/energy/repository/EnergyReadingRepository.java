package com.powersphere.energy.repository;

import com.powersphere.energy.entity.EnergyReading;
import com.powersphere.energy.enums.QualityStatus;
import com.powersphere.energy.enums.ReadingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long>,
        JpaSpecificationExecutor<EnergyReading> {

    // Find all active readings with pagination
    Page<EnergyReading> findByActiveTrue(Pageable pageable);

    // Find by meter ID
    Page<EnergyReading> findByMeterIdAndActiveTrue(Long meterId, Pageable pageable);

    List<EnergyReading> findByMeterIdAndActiveTrue(Long meterId);

    // Find latest reading for a meter
    Optional<EnergyReading> findTopByMeterIdAndActiveTrueOrderByReadingTimestampDesc(Long meterId);

    // Find reading history for a meter
    List<EnergyReading> findByMeterIdAndActiveTrueOrderByReadingTimestampDesc(Long meterId);

    // Find by reading type
    Page<EnergyReading> findByReadingTypeAndActiveTrue(ReadingType readingType, Pageable pageable);

    // Find by date range
    Page<EnergyReading> findByReadingTimestampBetweenAndActiveTrue(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    // Find by quality status
    Page<EnergyReading> findByQualityStatusAndActiveTrue(QualityStatus qualityStatus, Pageable pageable);

    // Check for duplicate readings (same meter, same timestamp)
    @Query("SELECT COUNT(e) > 0 FROM EnergyReading e WHERE e.meterId = :meterId " +
           "AND e.readingTimestamp = :timestamp AND e.active = true")
    boolean existsByMeterIdAndReadingTimestamp(
            @Param("meterId") Long meterId,
            @Param("timestamp") LocalDateTime timestamp);

    // Check for duplicate readings excluding a specific ID (for updates)
    @Query("SELECT COUNT(e) > 0 FROM EnergyReading e WHERE e.meterId = :meterId " +
           "AND e.readingTimestamp = :timestamp AND e.id != :excludeId AND e.active = true")
    boolean existsByMeterIdAndReadingTimestampExcludingId(
            @Param("meterId") Long meterId,
            @Param("timestamp") LocalDateTime timestamp,
            @Param("excludeId") Long excludeId);

    // Find readings by meter and date range
    List<EnergyReading> findByMeterIdAndReadingTimestampBetweenAndActiveTrueOrderByReadingTimestampAsc(
            Long meterId, LocalDateTime startDate, LocalDateTime endDate);

    // Count readings for a meter
    long countByMeterIdAndActiveTrue(Long meterId);

    // Find previous reading for consumption calculation
    Optional<EnergyReading> findTopByMeterIdAndReadingTimestampBeforeAndActiveTrueOrderByReadingTimestampDesc(
            Long meterId, LocalDateTime timestamp);
}
