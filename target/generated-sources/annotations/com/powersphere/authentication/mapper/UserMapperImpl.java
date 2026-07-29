package com.powersphere.authentication.mapper;

import com.powersphere.authentication.dto.request.RegisterRequest;
import com.powersphere.authentication.dto.response.LoginResponse;
import com.powersphere.authentication.dto.response.RegisterResponse;
import com.powersphere.authentication.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-29T17:58:33+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User registerRequestToUser(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( request.getFirstName() );
        user.lastName( request.getLastName() );
        user.email( request.getEmail() );
        user.username( request.getUsername() );
        user.password( request.getPassword() );
        user.phone( request.getPhone() );

        user.status( "ACTIVE" );
        user.enabled( true );
        user.accountLocked( false );
        user.emailVerified( false );
        user.failedLoginAttempts( 0 );
        user.isActive( true );

        return user.build();
    }

    @Override
    public RegisterResponse userToRegisterResponse(User user) {
        if ( user == null ) {
            return null;
        }

        RegisterResponse.RegisterResponseBuilder registerResponse = RegisterResponse.builder();

        registerResponse.id( user.getId() );
        registerResponse.firstName( user.getFirstName() );
        registerResponse.lastName( user.getLastName() );
        registerResponse.email( user.getEmail() );
        registerResponse.username( user.getUsername() );

        return registerResponse.build();
    }

    @Override
    public LoginResponse userToLoginResponse(User user) {
        if ( user == null ) {
            return null;
        }

        LoginResponse.LoginResponseBuilder loginResponse = LoginResponse.builder();

        loginResponse.id( user.getId() );
        loginResponse.firstName( user.getFirstName() );
        loginResponse.lastName( user.getLastName() );
        loginResponse.email( user.getEmail() );
        loginResponse.username( user.getUsername() );

        loginResponse.roles( rolesToNames(user.getRoles()) );
        loginResponse.tokenType( "Bearer" );

        return loginResponse.build();
    }
}
