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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamServiceImpl Unit Tests")
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TeamMapper teamMapper;

    private TeamServiceImpl teamService;

    private static final UUID TEAM_ID = UUID.randomUUID();
    private static final UUID DEPT_ID = UUID.randomUUID();
    private static final String TEAM_CODE = "TEAM001";
    private static final String TEAM_NAME = "Alpha Team";

    private Department department;
    private Team team;
    private TeamRequest request;
    private TeamResponse response;

    @BeforeEach
    void setUp() {
        teamService = new TeamServiceImpl(teamRepository, departmentRepository, teamMapper);

        department = Department.builder()
                .id(DEPT_ID)
                .code("DEPT001")
                .name("Engineering")
                .build();

        team = Team.builder()
                .id(TEAM_ID)
                .code(TEAM_CODE)
                .name(TEAM_NAME)
                .department(department)
                .isActive(true)
                .build();

        request = TeamRequest.builder()
                .code(TEAM_CODE)
                .name(TEAM_NAME)
                .departmentId(DEPT_ID)
                .build();

        response = TeamResponse.builder()
                .id(TEAM_ID)
                .code(TEAM_CODE)
                .name(TEAM_NAME)
                .departmentId(DEPT_ID)
                .departmentName("Engineering")
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Create Team")
    class CreateTeam {

        @Test
        @DisplayName("Should create team successfully")
        void shouldCreateSuccessfully() {
            when(teamRepository.existsByCode(TEAM_CODE)).thenReturn(false);
            when(departmentRepository.findById(DEPT_ID)).thenReturn(Optional.of(department));
            when(teamMapper.toEntity(request)).thenReturn(team);
            when(teamRepository.save(any(Team.class))).thenReturn(team);
            when(teamMapper.toResponse(team)).thenReturn(response);

            var result = teamService.createTeam(request);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(TEAM_CODE);
        }

        @Test
        @DisplayName("Should throw when duplicate code")
        void shouldThrowWhenDuplicateCode() {
            when(teamRepository.existsByCode(TEAM_CODE)).thenReturn(true);

            assertThatThrownBy(() -> teamService.createTeam(request))
                    .isInstanceOf(DuplicateTeamException.class);
        }

        @Test
        @DisplayName("Should throw when department not found")
        void shouldThrowWhenDeptNotFound() {
            when(teamRepository.existsByCode(TEAM_CODE)).thenReturn(false);
            when(departmentRepository.findById(DEPT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.createTeam(request))
                    .isInstanceOf(DepartmentNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Get Team")
    class GetTeam {

        @Test
        @DisplayName("Should get by ID")
        void shouldGetById() {
            when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
            when(teamMapper.toResponse(team)).thenReturn(response);

            var result = teamService.getTeamById(TEAM_ID);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(TEAM_ID);
        }

        @Test
        @DisplayName("Should throw when not found")
        void shouldThrowWhenNotFound() {
            when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.getTeamById(TEAM_ID))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Team")
    class DeleteTeam {

        @Test
        @DisplayName("Should soft-delete team")
        void shouldSoftDelete() {
            when(teamRepository.findById(TEAM_ID)).thenReturn(Optional.of(team));
            when(teamRepository.save(any(Team.class))).thenReturn(team);

            teamService.deleteTeam(TEAM_ID);

            verify(teamRepository).save(any(Team.class));
        }
    }
}
