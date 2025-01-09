package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.UserRole;

public record UserAuthResponse(
        Long id,
        String username,
        String nickname,
        String email,
        UserRole role
) {

}
