package com.takeit.auth.presentation.request;

import com.takeit.auth.application.dto.CreateUserDto;
import com.takeit.auth.domain.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerSignUpRequest(
        @NotNull
        @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "username은 소문자와 숫자로 구성된 4자 이상, 10자 이하여야 합니다.")
        String username,

        @NotNull
        @Size(max = 100, message = "nickname은 100자 이하여야 합니다.")
        String nickname,

        @NotNull
        @Email(message = "유효한 이메일 형식이어야 합니다.")
        @Size(max = 255, message = "email은 255자 이하여야 합니다.")
        String email,

        @NotNull
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
                message = "password는 대문자, 소문자, 숫자, 특수문자를 포함한 8자 이상, 15자 이하여야 합니다."
        )
        String password,

        UserRole role
) {
    public CustomerSignUpRequest {
        // role 기본값 설정
        if (role == null) {
            role = UserRole.CUSTOMER;
        }
    }

    public CreateUserDto toDto() {
        return new CreateUserDto(username, nickname, email, password, role);
    }
}
