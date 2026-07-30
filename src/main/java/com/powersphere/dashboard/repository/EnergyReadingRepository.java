package com.powersphere.dashboard.repository;

import com.powersphere.dashboard.entity.EnergyReadingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReadingEntity, Long> {

    @Query("SELECT COALESCE(SUM(e.consumption), 0) FROM EnergyReadingEntity e WHERE e.readingDate >= :start AND e.readingDate < :end")
    double sumConsumptionBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(e.consumption), 0) FROM EnergyReadingEntity e WHERE e.readingDate >= :start")
    double sumConsumptionSince(@Param("start") LocalDateTime start);

    @Query(value = "SELECT CAST(e.readingDate AS date) as readingDay, SUM(e.consumption) as totalConsumption " +
            "FROM EnergyReadingEntity e " +
            "WHERE e.readingDate >= :start AND e.readingDate < :end " +
            "GROUP BY CAST(e.readingDate AS date) " +
            "ORDER BY readingDay ASC")
    List<Object[]> sumConsumptionByDay(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT FUNCTION('YEAR', e.readingDate) as yr, FUNCTION('MONTH', e.readingDate) as mo, SUM(e.consumption) " +
            "FROM EnergyReadingEntity e " +
            "WHERE e.readingDate >= :start AND e.readingDate < :end " +
            "GROUP BY FUNCTION('YEAR', e.readingDate), FUNCTION('MONTH', e.readingDate) " +
            "ORDER BY yr ASC, mo ASC")
    List<Object[]> sumConsumptionByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT FUNCTION('YEAR', e.readingDate) as yr, SUM(e.consumption) " +
            "FROM EnergyReadingEntity e " +
            "WHERE e.readingDate >= :start AND e.readingDate < :end " +
            "GROUP BY FUNCTION('YEAR', e.readingDate) " +
            "ORDER BY yr ASC")
    List<Object[]> sumConsumptionByYear(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT e.meterId, SUM(e.consumption) FROM EnergyReadingEntity e " +
            "WHERE e.readingDate >= :start AND e.readingDate < :end " +
            "GROUP BY e.meterId ORDER BY SUM(e.consumption) DESC")
    List<Object[]> topConsumers(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("SELECT e.meterId, SUM(e.consumption) FROM EnergyReadingEntity e " +
            "WHERE e.readingDate >= :start AND e.readingDate < :end " +
            "GROUP BY e.meterId ORDER BY SUM(e.consumption) DESC")
    List<Object[]> topConsumersUnbounded(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT FUNCTION('HOUR', e.readingDate) as hr, AVG(e.consumption) " +
            "FROM EnergyReadingEntity e " +
            "GROUP BY FUNCTION('HOUR', e.readingDate) " +
            "ORDER BY AVG(e.consumption) DESC")
    List<Object[]> avgConsumptionByHour();

    @Query("SELECT COALESCE(SUM(e.consumption), 0) FROM EnergyReadingEntity e " +
            "WHERE e.meterId IN :meterIds AND e.readingDate >= :start AND e.readingDate < :end")
    double sumConsumptionByMeterIds(@Param("meterIds") List<Long> meterIds,
                                     @Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    @Query("SELECT e.meterId, COALESCE(SUM(e.consumption), 0) FROM EnergyReadingEntity e " +
            "WHERE e.meterId IN :meterIds AND e.readingDate >= :start AND e.readingDate < :end " +
            "GROUP BY e.meterId")
    List<Object[]> sumConsumptionByMeterIdsGrouped(@Param("meterIds") List<Long> meterIds,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);
}
