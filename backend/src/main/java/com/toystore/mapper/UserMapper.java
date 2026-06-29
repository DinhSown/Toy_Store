package com.toystore.mapper;

import com.toystore.dto.request.RegisterRequest;
import com.toystore.dto.request.UpdateProfileRequest;
import com.toystore.dto.response.UserResponse;
import com.toystore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "password", ignore = true)  // Encode mật khẩu riêng ở Service
    @Mapping(target = "role", ignore = true)       // Set mặc định ở Service
    @Mapping(target = "isActive", ignore = true)   // Set mặc định ở Service
    @Mapping(target = "avatarUrl", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)      // Không cho phép cập nhật email
    @Mapping(target = "password", ignore = true)   // Mật khẩu cập nhật bằng API riêng biệt
    @Mapping(target = "role", ignore = true)       // Role không được tự ý sửa ở profile update
    @Mapping(target = "isActive", ignore = true)   // Trạng thái hoạt động không đổi ở profile update
    @Mapping(target = "createdAt", ignore = true)  // Audit field tự động cập nhật
    @Mapping(target = "updatedAt", ignore = true)  // Audit field tự động cập nhật
    void updateEntityFromRequest(UpdateProfileRequest request, @MappingTarget User user);
}
