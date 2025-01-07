package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.UserRole;

public record CreateUserDto(
        String username,
        String nickname,
        String email,
        String password,
        UserRole role
) {

}
