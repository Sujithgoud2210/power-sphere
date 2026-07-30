package com.powersphere.users.mapper;

import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import com.powersphere.organization.entity.Department;
import com.powersphere.organization.entity.Organization;
import com.powersphere.organization.entity.Team;
import com.powersphere.users.dto.response.UserProfileResponse;
import com.powersphere.users.entity.UserProfile;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-30T09:25:06+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Override
    public UserProfileResponse toResponse(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UserProfileResponse.UserProfileResponseBuilder userProfileResponse = UserProfileResponse.builder();

        userProfileResponse.userId( userProfileUserId( userProfile ) );
        userProfileResponse.username( userProfileUserUsername( userProfile ) );
        userProfileResponse.email( userProfileUserEmail( userProfile ) );
        userProfileResponse.firstName( userProfileUserFirstName( userProfile ) );
        userProfileResponse.lastName( userProfileUserLastName( userProfile ) );
        userProfileResponse.phone( userProfileUserPhone( userProfile ) );
        userProfileResponse.status( userProfileUserStatus( userProfile ) );
        userProfileResponse.enabled( userProfileUserEnabled( userProfile ) );
        userProfileResponse.accountLocked( userProfileUserAccountLocked( userProfile ) );
        userProfileResponse.emailVerified( userProfileUserEmailVerified( userProfile ) );
        userProfileResponse.lastLogin( userProfileUserLastLogin( userProfile ) );
        Set<Role> roles = userProfileUserRoles( userProfile );
        userProfileResponse.roles( rolesToNames( roles ) );
        userProfileResponse.organizationId( userProfileOrganizationId( userProfile ) );
        userProfileResponse.organizationName( userProfileOrganizationOrganizationName( userProfile ) );
        userProfileResponse.departmentId( userProfileDepartmentId( userProfile ) );
        userProfileResponse.departmentName( userProfileDepartmentName( userProfile ) );
        userProfileResponse.teamId( userProfileTeamId( userProfile ) );
        userProfileResponse.teamName( userProfileTeamName( userProfile ) );
        userProfileResponse.id( userProfile.getId() );
        userProfileResponse.employeeId( userProfile.getEmployeeId() );
        userProfileResponse.designation( userProfile.getDesignation() );
        userProfileResponse.joiningDate( userProfile.getJoiningDate() );
        userProfileResponse.dateOfBirth( userProfile.getDateOfBirth() );
        userProfileResponse.gender( userProfile.getGender() );
        userProfileResponse.address( userProfile.getAddress() );
        userProfileResponse.emergencyContact( userProfile.getEmergencyContact() );
        userProfileResponse.profileImageUrl( userProfile.getProfileImageUrl() );
        userProfileResponse.createdAt( userProfile.getCreatedAt() );
        userProfileResponse.updatedAt( userProfile.getUpdatedAt() );

        return userProfileResponse.build();
    }

    private UUID userProfileUserId(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String userProfileUserUsername(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }

    private String userProfileUserEmail(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getEmail();
    }

    private String userProfileUserFirstName(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFirstName();
    }

    private String userProfileUserLastName(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getLastName();
    }

    private String userProfileUserPhone(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getPhone();
    }

    private String userProfileUserStatus(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getStatus();
    }

    private boolean userProfileUserEnabled(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return false;
        }
        return user.isEnabled();
    }

    private boolean userProfileUserAccountLocked(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return false;
        }
        return user.isAccountLocked();
    }

    private boolean userProfileUserEmailVerified(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return false;
        }
        return user.isEmailVerified();
    }

    private LocalDateTime userProfileUserLastLogin(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getLastLogin();
    }

    private Set<Role> userProfileUserRoles(UserProfile userProfile) {
        User user = userProfile.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getRoles();
    }

    private UUID userProfileOrganizationId(UserProfile userProfile) {
        Organization organization = userProfile.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getId();
    }

    private String userProfileOrganizationOrganizationName(UserProfile userProfile) {
        Organization organization = userProfile.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getOrganizationName();
    }

    private UUID userProfileDepartmentId(UserProfile userProfile) {
        Department department = userProfile.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getId();
    }

    private String userProfileDepartmentName(UserProfile userProfile) {
        Department department = userProfile.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getName();
    }

    private UUID userProfileTeamId(UserProfile userProfile) {
        Team team = userProfile.getTeam();
        if ( team == null ) {
            return null;
        }
        return team.getId();
    }

    private String userProfileTeamName(UserProfile userProfile) {
        Team team = userProfile.getTeam();
        if ( team == null ) {
            return null;
        }
        return team.getName();
    }
}
