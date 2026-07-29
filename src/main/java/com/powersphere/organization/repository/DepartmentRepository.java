package com.powersphere.organization.repository;

import com.powersphere.organization.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);

    List<Department> findByOrganizationId(UUID organizationId);

    List<Department> findByIsActiveTrue();
}
