package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.UserRole;

public record UserRoleResponse(
        String username,
        UserRole role
) {

}
