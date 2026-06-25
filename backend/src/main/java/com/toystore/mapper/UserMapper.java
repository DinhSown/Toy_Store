package com.toystore.mapper;

import com.toystore.dto.request.UserRegisterRequest;
import com.toystore.dto.response.UserResponse;
import com.toystore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "password", ignore = true) // Sẽ encode mật khẩu riêng ở Service
    @Mapping(target = "role", ignore = true)     // Sẽ set mặc định hoặc gán ở Service
    @Mapping(target = "isActive", ignore = true) // Sẽ set mặc định ở Service
    @Mapping(target = "avatarUrl", ignore = true)
    User toEntity(UserRegisterRequest request);
}
