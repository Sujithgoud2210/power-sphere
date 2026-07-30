package com.powersphere.dashboard.repository;

import com.powersphere.dashboard.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

    long countByActiveTrue();

    @Query("SELECT o.organizationId, o.name FROM OrganizationEntity o WHERE o.organizationId IN :orgIds")
    List<Object[]> findNamesByIds(@Param("orgIds") List<Long> orgIds);

    @Query("SELECT o FROM OrganizationEntity o WHERE o.active = true")
    List<OrganizationEntity> findAllActive();
}
