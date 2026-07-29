package com.powersphere.organization.repository;

import com.powersphere.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findByOrganizationCode(String organizationCode);

    boolean existsByOrganizationCode(String organizationCode);

    List<Organization> findByIsActiveTrue();

    List<Organization> findByOrganizationNameContainingIgnoreCase(String organizationName);

    List<Organization> findByStatus(String status);
}
