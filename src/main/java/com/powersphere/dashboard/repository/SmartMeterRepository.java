package com.powersphere.dashboard.repository;

import com.powersphere.dashboard.entity.SmartMeterEntity;
import com.powersphere.dashboard.entity.SmartMeterEntity.MeterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmartMeterRepository extends JpaRepository<SmartMeterEntity, Long> {

    long countByStatus(MeterStatus status);

    long countByOrganizationIdAndStatus(Long organizationId, MeterStatus status);

    @Query("SELECT COUNT(sm) FROM SmartMeterEntity sm WHERE sm.status = :status")
    long countByMeterStatus(@Param("status") MeterStatus status);

    @Query("SELECT sm.organizationId, COUNT(sm) FROM SmartMeterEntity sm GROUP BY sm.organizationId")
    List<Object[]> countByOrganization();

    @Query("SELECT sm.organizationId, COUNT(sm) FROM SmartMeterEntity sm WHERE sm.organizationId IN :orgIds AND sm.status = :status GROUP BY sm.organizationId")
    List<Object[]> countByOrganizationIdsAndStatus(@Param("orgIds") List<Long> orgIds, @Param("status") MeterStatus status);

    @Query("SELECT sm.status, COUNT(sm) FROM SmartMeterEntity sm GROUP BY sm.status")
    List<Object[]> countByStatusGrouped();

    @Query("SELECT sm.meterId, sm.organizationId FROM SmartMeterEntity sm")
    List<Object[]> findAllMeterIdsWithOrganizationIds();
}
