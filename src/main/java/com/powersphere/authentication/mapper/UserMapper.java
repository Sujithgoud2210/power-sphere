package com.powersphere.authentication.mapper;

import com.powersphere.authentication.dto.request.RegisterRequest;
import com.powersphere.authentication.dto.response.LoginResponse;
import com.powersphere.authentication.dto.response.RegisterResponse;
import com.powersphere.authentication.entity.Role;
import com.powersphere.authentication.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountLocked", constant = "false")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "failedLoginAttempts", constant = "0")
    @Mapping(target = "isActive", constant = "true")
    User registerRequestToUser(RegisterRequest request);

    RegisterResponse userToRegisterResponse(User user);

    @Mapping(target = "roles", expression = "java(rolesToNames(user.getRoles()))")
    @Mapping(target = "tokenType", constant = "Bearer")
    LoginResponse userToLoginResponse(User user);

    default Set<String> rolesToNames(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
