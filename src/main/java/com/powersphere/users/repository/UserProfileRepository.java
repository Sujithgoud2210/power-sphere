package com.powersphere.users.repository;

import com.powersphere.users.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    Optional<UserProfile> findByUserId(UUID userId);

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    Optional<UserProfile> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    List<UserProfile> findByOrganizationId(UUID organizationId);

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    List<UserProfile> findByDepartmentId(UUID departmentId);

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    List<UserProfile> findByTeamId(UUID teamId);

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    @Query("SELECT up FROM UserProfile up WHERE " +
            "LOWER(up.user.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.designation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<UserProfile> searchUsers(@Param("searchTerm") String searchTerm);

    @EntityGraph(attributePaths = {"user", "organization", "department", "team"})
    @Query("SELECT up FROM UserProfile up WHERE " +
            "LOWER(up.user.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.user.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.employeeId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(up.designation) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<UserProfile> searchUsersPaginated(@Param("searchTerm") String searchTerm, Pageable pageable);
}
