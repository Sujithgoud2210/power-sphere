package com.powersphere.authentication.mapper;

import com.powersphere.authentication.dto.request.RegisterRequest;
import com.powersphere.authentication.dto.response.LoginResponse;
import com.powersphere.authentication.dto.response.RegisterResponse;
import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountLocked", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "failedLoginAttempts", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User registerRequestToUser(RegisterRequest request);

    RegisterResponse userToRegisterResponse(User user);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToNames")
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "tokenType", constant = "Bearer")
    @Mapping(target = "expiresIn", ignore = true)
    LoginResponse userToLoginResponse(User user);

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
