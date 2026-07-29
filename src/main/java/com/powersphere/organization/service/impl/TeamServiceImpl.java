package com.powersphere.organization.service.impl;

import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Team;
import com.powersphere.organization.exception.DepartmentNotFoundException;
import com.powersphere.organization.exception.DuplicateTeamException;
import com.powersphere.organization.exception.TeamNotFoundException;
import com.powersphere.organization.mapper.TeamMapper;
import com.powersphere.organization.repository.DepartmentRepository;
import com.powersphere.organization.repository.TeamRepository;
import com.powersphere.organization.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository,
                           DepartmentRepository departmentRepository,
                           TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.departmentRepository = departmentRepository;
        this.teamMapper = teamMapper;
    }

    @Override
    public TeamResponse createTeam(TeamRequest request) {
        log.info("Creating team with code: {} in department: {}", request.getCode(), request.getDepartmentId());

        if (teamRepository.existsByCode(request.getCode())) {
            throw new DuplicateTeamException(
                    "Team with code '" + request.getCode() + "' already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with id: " + request.getDepartmentId()));

        Team team = teamMapper.toEntity(request);
        team.setDepartment(department);

        Team savedTeam = teamRepository.save(team);
        log.info("Team created with id: {}", savedTeam.getId());

        return teamMapper.toResponse(savedTeam);
    }

    @Override
    public TeamResponse updateTeam(UUID id, TeamRequest request) {
        log.info("Updating team with id: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(
                        "Team not found with id: " + id));

        if (!team.getCode().equals(request.getCode())
                && teamRepository.existsByCode(request.getCode())) {
            throw new DuplicateTeamException(
                    "Team with code '" + request.getCode() + "' already exists");
        }

        if (request.getDepartmentId() != null
                && (team.getDepartment() == null
                || !team.getDepartment().getId().equals(request.getDepartmentId()))) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(
                            "Department not found with id: " + request.getDepartmentId()));
            team.setDepartment(department);
        }

        teamMapper.updateEntity(team, request);
        Team updatedTeam = teamRepository.save(team);
        log.info("Team updated with id: {}", updatedTeam.getId());

        return teamMapper.toResponse(updatedTeam);
    }

    @Override
    @Transactional
    public void deleteTeam(UUID id) {
        log.info("Deleting team with id: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(
                        "Team not found with id: " + id));

        team.setIsActive(false);
        teamRepository.save(team);
        log.info("Team soft-deleted with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(UUID id) {
        log.debug("Fetching team by id: {}", id);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(
                        "Team not found with id: " + id));

        return teamMapper.toResponse(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamsByDepartment(UUID departmentId) {
        log.debug("Fetching teams for department: {}", departmentId);

        return teamRepository.findByDepartmentIdAndIsActiveTrue(departmentId).stream()
                .map(teamMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> searchTeams(String name) {
        log.debug("Searching teams by name: {}", name);

        return teamRepository.findByNameContainingIgnoreCase(name).stream()
                .map(teamMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {
        log.debug("Fetching all active teams");

        return teamRepository.findByIsActiveTrue().stream()
                .map(teamMapper::toResponse)
                .collect(Collectors.toList());
    }
}
