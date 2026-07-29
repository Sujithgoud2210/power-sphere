package com.powersphere.dashboard.repository;

import com.powersphere.dashboard.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    long countByActiveTrue();

    long countByOrganizationId(Long organizationId);

    long countByOrganizationIdAndActiveTrue(Long organizationId);

    @Query("SELECT u.organizationId, COUNT(u) FROM UserEntity u WHERE u.organizationId IN :orgIds GROUP BY u.organizationId")
    List<Object[]> countByOrganizationIds(@Param("orgIds") List<Long> orgIds);

    @Query("SELECT u.organizationId, COUNT(u) FROM UserEntity u WHERE u.organizationId IN :orgIds AND u.active = true GROUP BY u.organizationId")
    List<Object[]> countActiveByOrganizationIds(@Param("orgIds") List<Long> orgIds);

    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.createdAt >= :since")
    long countUsersSince(@Param("since") LocalDateTime since);
}
