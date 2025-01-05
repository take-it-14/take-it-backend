package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.entity.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String nickname;
    private String email;
    private UserRole role;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
