package com.powersphere.organization.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;
import com.powersphere.organization.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = ApplicationConstants.API_BASE_PATH + "/teams",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Teams", description = "Team management endpoints")
public class TeamController {

    private static final Logger log = LoggerFactory.getLogger(TeamController.class);

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new team",
            description = "Creates a new team within a department")
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Valid @RequestBody TeamRequest request) {
        log.debug("POST /api/v1/teams - code: {}", request.getCode());
        TeamResponse response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Team created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update a team",
            description = "Updates an existing team by ID")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @PathVariable UUID id,
            @Valid @RequestBody TeamRequest request) {
        log.debug("PUT /api/v1/teams/{}", id);
        TeamResponse response = teamService.updateTeam(id, request);
        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a team",
            description = "Soft-deletes a team by ID")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(@PathVariable UUID id) {
        log.debug("DELETE /api/v1/teams/{}", id);
        teamService.deleteTeam(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Team deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get team by ID",
            description = "Retrieves team details by ID")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(@PathVariable UUID id) {
        log.debug("GET /api/v1/teams/{}", id);
        TeamResponse response = teamService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all teams",
            description = "Retrieves all active teams")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {
        log.debug("GET /api/v1/teams");
        List<TeamResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(ApiResponse.success(teams));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search teams",
            description = "Search teams by name")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> searchTeams(
            @RequestParam String name) {
        log.debug("GET /api/v1/teams/search?name={}", name);
        List<TeamResponse> teams = teamService.searchTeams(name);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }

    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get teams by department",
            description = "Retrieves all teams belonging to a department")
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getTeamsByDepartment(
            @PathVariable UUID departmentId) {
        log.debug("GET /api/v1/teams/by-department/{}", departmentId);
        List<TeamResponse> teams = teamService.getTeamsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }
}
