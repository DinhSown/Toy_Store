package com.toystore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO để cập nhật thông tin hồ sơ cá nhân của user.
 * Sử dụng ở API: PUT /api/auth/profile
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String phone;

    private String avatarUrl;
}
