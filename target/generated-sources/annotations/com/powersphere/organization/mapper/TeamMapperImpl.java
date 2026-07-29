package com.powersphere.organization.mapper;

import com.powersphere.organization.dto.request.TeamRequest;
import com.powersphere.organization.dto.response.TeamResponse;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Team;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T17:58:33+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class TeamMapperImpl implements TeamMapper {

    @Override
    public Team toEntity(TeamRequest request) {
        if ( request == null ) {
            return null;
        }

        Team.TeamBuilder team = Team.builder();

        team.name( request.getName() );
        team.code( request.getCode() );
        team.description( request.getDescription() );
        team.teamLead( request.getTeamLead() );

        team.isActive( true );

        return team.build();
    }

    @Override
    public TeamResponse toResponse(Team team) {
        if ( team == null ) {
            return null;
        }

        TeamResponse.TeamResponseBuilder teamResponse = TeamResponse.builder();

        teamResponse.departmentId( teamDepartmentId( team ) );
        teamResponse.departmentName( teamDepartmentName( team ) );
        teamResponse.id( team.getId() );
        teamResponse.name( team.getName() );
        teamResponse.code( team.getCode() );
        teamResponse.description( team.getDescription() );
        teamResponse.teamLead( team.getTeamLead() );
        teamResponse.isActive( team.getIsActive() );
        teamResponse.createdAt( team.getCreatedAt() );
        teamResponse.updatedAt( team.getUpdatedAt() );
        teamResponse.createdBy( team.getCreatedBy() );
        teamResponse.updatedBy( team.getUpdatedBy() );

        return teamResponse.build();
    }

    @Override
    public void updateEntity(Team team, TeamRequest request) {
        if ( request == null ) {
            return;
        }

        team.setName( request.getName() );
        team.setCode( request.getCode() );
        team.setDescription( request.getDescription() );
        team.setTeamLead( request.getTeamLead() );
    }

    private UUID teamDepartmentId(Team team) {
        Department department = team.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getId();
    }

    private String teamDepartmentName(Team team) {
        Department department = team.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getName();
    }
}
