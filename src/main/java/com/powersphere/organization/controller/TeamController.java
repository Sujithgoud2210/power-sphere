package com.powersphere.organization.controller;

import com.powersphere.common.constant.ApplicationConstants;
import com.powersphere.common.dto.ApiResponse;
import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;
import com.powersphere.organization.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = "Creates a new team within a department with the provided name, code, and team lead.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Team created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input - validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Team with same code already exists")
    })
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
            description = "Updates an existing team's details including name, code, description, and team lead by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @Parameter(description = "Team UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id,
            @Valid @RequestBody TeamRequest request) {
        log.debug("PUT /api/v1/teams/{}", id);
        TeamResponse response = teamService.updateTeam(id, request);
        return ResponseEntity.ok(ApiResponse.success("Team updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a team",
            description = "Soft-deletes a team by ID, marking it as inactive without removing data from the database.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Team not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Team has active members")
    })
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @Parameter(description = "Team UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id) {
        log.debug("DELETE /api/v1/teams/{}", id);
        teamService.deleteTeam(id);
        return ResponseEntity.ok(ApiResponse.<Void>success("Team deleted successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get team by ID",
            description = "Retrieves detailed information about a team including name, code, team lead, and associated department.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Team not found")
    })
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(
            @Parameter(description = "Team UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID id) {
        log.debug("GET /api/v1/teams/{}", id);
        TeamResponse response = teamService.getTeamById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get all teams",
            description = "Retrieves a list of all active teams in the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Teams retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getAllTeams() {
        log.debug("GET /api/v1/teams");
        List<TeamResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(ApiResponse.success(teams));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Search teams",
            description = "Search teams by name. Returns matching teams whose name contains the search term.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<TeamResponse>>> searchTeams(
            @Parameter(description = "Team name search term", example = "Platform", required = true) @RequestParam String name) {
        log.debug("GET /api/v1/teams/search?name={}", name);
        List<TeamResponse> teams = teamService.searchTeams(name);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }

    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'OPERATOR', 'VIEWER')")
    @Operation(summary = "Get teams by department",
            description = "Retrieves all teams belonging to a specific department.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getTeamsByDepartment(
            @Parameter(description = "Department UUID", example = "550e8400-e29b-41d4-a716-446655440000", required = true) @PathVariable UUID departmentId) {
        log.debug("GET /api/v1/teams/by-department/{}", departmentId);
        List<TeamResponse> teams = teamService.getTeamsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success(teams));
    }
}
