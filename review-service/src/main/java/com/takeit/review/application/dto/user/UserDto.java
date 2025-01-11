package com.takeit.review.application.dto.user;

public record UserDto (
        Long id,
        String username,
        String role
) {
}
