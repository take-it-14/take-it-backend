package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.entity.UserRole;

public record UserResponse (
    String username,
    String nickname,
    String email,
    UserRole role
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getUsername(), user.getNickname(), user.getEmail(), user.getRole());
    }
}
