package com.toystore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String phone;

    private String avatarUrl;
}
