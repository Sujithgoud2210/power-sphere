package com.powersphere.organization.controller;

import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;
import com.powersphere.organization.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments/{departmentId}/teams")
@RequiredArgsConstructor
@Tag(name = "Team", description = "Team management APIs")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "Create a new team within a department")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @PathVariable UUID departmentId,
            @Valid @RequestBody TeamRequest request) {
        log.info("REST request to create team in department: {}", departmentId);
        var response = teamService.createTeam(departmentId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Team created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing team")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @PathVariable UUID departmentId,
            @PathVariable UUID id,
            @Valid @RequestBody TeamRequest request) {
        log.info("REST request to update team with id: {}", id);
        var response = teamService.updateTeam(id, request);
        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a team")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @PathVariable UUID departmentId,
            @PathVariable UUID id) {
        log.info("REST request to delete team with id: {}", id);
        teamService.deleteTeam(id);
        return ResponseEntity.ok(ApiResponse.success("Team deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team by ID")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(
            @PathVariable UUID departmentId,
            @PathVariable UUID id) {
        log.info("REST request to get team by id: {}", id);
        var response = teamService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all teams for a department")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getTeamsByDepartment(
            @PathVariable UUID departmentId) {
        log.info("REST request to get teams for department: {}", departmentId);
        var response = teamService.getTeamsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
