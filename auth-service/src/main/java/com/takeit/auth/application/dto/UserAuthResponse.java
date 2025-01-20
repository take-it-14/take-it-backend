package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.entity.UserRole;

public record UserAuthResponse(
        Long id,
        String username,
        String nickname,
        String email,
        UserRole role
) {

    public static UserAuthResponse from(User user) {
        return new UserAuthResponse(user.getId(), user.getUsername(), user.getNickname(), user.getEmail(), user.getRole());
    }
}
