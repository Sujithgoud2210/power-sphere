package com.powersphere.users.mapper;

import com.powersphere.authentication.entity.Role;
import com.powersphere.users.dto.response.UserProfileResponse;
import com.powersphere.users.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "status", source = "user.status")
    @Mapping(target = "enabled", source = "user.enabled")
    @Mapping(target = "accountLocked", source = "user.accountLocked")
    @Mapping(target = "emailVerified", source = "user.emailVerified")
    @Mapping(target = "lastLogin", source = "user.lastLogin")
    @Mapping(target = "roles", source = "user.roles", qualifiedByName = "rolesToNames")
    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "organizationName", source = "organization.organizationName")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.name")
    UserProfileResponse toResponse(UserProfile userProfile);

    @Named("rolesToNames")
    default List<String> rolesToNames(Set<Role> roles) {
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
