package com.powersphere.organization.service;

import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;

import java.util.List;
import java.util.UUID;

public interface TeamService {

    TeamResponse createTeam(UUID departmentId, TeamRequest request);

    TeamResponse updateTeam(UUID id, TeamRequest request);

    void deleteTeam(UUID id);

    TeamResponse getTeamById(UUID id);

    List<TeamResponse> getTeamsByDepartment(UUID departmentId);
}
