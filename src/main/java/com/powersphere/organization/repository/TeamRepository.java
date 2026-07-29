package com.powersphere.organization.repository;

import com.powersphere.organization.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    Optional<Team> findByCode(String code);

    boolean existsByCode(String code);

    List<Team> findByDepartmentId(UUID departmentId);

    List<Team> findByNameContainingIgnoreCase(String name);

    List<Team> findByDepartmentIdAndIsActiveTrue(UUID departmentId);

    List<Team> findByIsActiveTrue();
}
