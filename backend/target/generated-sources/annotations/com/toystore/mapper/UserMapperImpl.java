package com.toystore.mapper;

import com.toystore.dto.request.RegisterRequest;
import com.toystore.dto.request.UpdateProfileRequest;
import com.toystore.dto.response.UserResponse;
import com.toystore.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-29T15:12:58+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( user.getId() );
        userResponse.email( user.getEmail() );
        userResponse.fullName( user.getFullName() );
        userResponse.phone( user.getPhone() );
        userResponse.avatarUrl( user.getAvatarUrl() );
        userResponse.role( user.getRole() );
        userResponse.isActive( user.getIsActive() );
        userResponse.createdAt( user.getCreatedAt() );
        userResponse.updatedAt( user.getUpdatedAt() );

        return userResponse.build();
    }

    @Override
    public User toEntity(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.getEmail() );
        user.fullName( request.getFullName() );
        user.phone( request.getPhone() );

        return user.build();
    }

    @Override
    public void updateEntityFromRequest(UpdateProfileRequest request, User user) {
        if ( request == null ) {
            return;
        }

        user.setFullName( request.getFullName() );
        user.setPhone( request.getPhone() );
        user.setAvatarUrl( request.getAvatarUrl() );
    }
}
