package com.powersphere.meter.repository;

import com.powersphere.meter.entity.SmartMeter;
import com.powersphere.meter.enums.ConnectionType;
import com.powersphere.meter.enums.MeterStatus;
import com.powersphere.meter.enums.MeterType;
import com.powersphere.meter.enums.PhaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SmartMeterRepository extends JpaRepository<SmartMeter, UUID> {

    Optional<SmartMeter> findByMeterNumber(String meterNumber);

    Optional<SmartMeter> findBySerialNumber(String serialNumber);

    boolean existsByMeterNumber(String meterNumber);

    boolean existsBySerialNumber(String serialNumber);

    Page<SmartMeter> findByIsActiveTrue(Pageable pageable);

    List<SmartMeter> findByOrganizationId(UUID organizationId);

    List<SmartMeter> findByAssignedUserId(UUID userId);

    List<SmartMeter> findByStatus(MeterStatus status);

    Page<SmartMeter> findByOrganizationId(UUID organizationId, Pageable pageable);

    // Search by meter number, serial number, manufacturer, or model
    @Query("SELECT m FROM SmartMeter m WHERE " +
            "(:searchTerm IS NULL OR LOWER(m.meterNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.serialNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<SmartMeter> searchMeters(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Dynamic filtering
    @Query("SELECT m FROM SmartMeter m WHERE " +
            "(:searchTerm IS NULL OR (LOWER(m.meterNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.serialNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(m.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) AND " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(:meterType IS NULL OR m.meterType = :meterType) AND " +
            "(:phaseType IS NULL OR m.phaseType = :phaseType) AND " +
            "(:connectionType IS NULL OR m.connectionType = :connectionType) AND " +
            "(:organizationId IS NULL OR m.organization.id = :organizationId) AND " +
            "(:assignedUserId IS NULL OR m.assignedUser.id = :assignedUserId) AND " +
            "(:city IS NULL OR LOWER(m.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:state IS NULL OR LOWER(m.state) LIKE LOWER(CONCAT('%', :state, '%'))) AND " +
            "(:country IS NULL OR LOWER(m.country) LIKE LOWER(CONCAT('%', :country, '%'))) AND " +
            "(:installationDateFrom IS NULL OR m.installationDate >= :installationDateFrom) AND " +
            "(:installationDateTo IS NULL OR m.installationDate <= :installationDateTo)")
    Page<SmartMeter> filterMeters(
            @Param("searchTerm") String searchTerm,
            @Param("status") MeterStatus status,
            @Param("meterType") MeterType meterType,
            @Param("phaseType") PhaseType phaseType,
            @Param("connectionType") ConnectionType connectionType,
            @Param("organizationId") UUID organizationId,
            @Param("assignedUserId") UUID assignedUserId,
            @Param("city") String city,
            @Param("state") String state,
            @Param("country") String country,
            @Param("installationDateFrom") LocalDateTime installationDateFrom,
            @Param("installationDateTo") LocalDateTime installationDateTo,
            Pageable pageable);
}
