package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.DuplicateTeamException;
import com.powersphere.organization.exception.TeamNotFoundException;
import com.powersphere.organization.mapper.TeamMapper;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.TeamRepository;
import com.powersphere.organization.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamMapper teamMapper;

    @Override
    @Transactional
    public TeamResponse createTeam(UUID departmentId, TeamRequest request) {
        log.debug("Creating team with code: {} in department: {}", request.getCode(), departmentId);

        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + departmentId));

        if (teamRepository.existsByCode(request.getCode())) {
            throw new DuplicateTeamException("Team with code '" + request.getCode() + "' already exists");
        }

        var team = teamMapper.toEntity(request);
        team.setDepartment(department);
        team = teamRepository.save(team);
        log.info("Team created successfully with id: {}", team.getId());
        return teamMapper.toResponse(team);
    }

    @Override
    @Transactional
    public TeamResponse updateTeam(UUID id, TeamRequest request) {
        log.debug("Updating team with id: {}", id);

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with id: " + id));

        teamMapper.updateEntity(team, request);
        team = teamRepository.save(team);
        log.info("Team updated successfully with id: {}", id);
        return teamMapper.toResponse(team);
    }

    @Override
    @Transactional
    public void deleteTeam(UUID id) {
        log.debug("Deleting team with id: {}", id);

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with id: " + id));

        team.setIsActive(false);
        teamRepository.save(team);
        log.info("Team soft-deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(UUID id) {
        log.debug("Fetching team by id: {}", id);

        var team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with id: " + id));

        return teamMapper.toResponse(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamsByDepartment(UUID departmentId) {
        log.debug("Fetching teams for department: {}", departmentId);

        return teamRepository.findByDepartmentId(departmentId)
                .stream()
                .map(teamMapper::toResponse)
                .toList();
    }
}
