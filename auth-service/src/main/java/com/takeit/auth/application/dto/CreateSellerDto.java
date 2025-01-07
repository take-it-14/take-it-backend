package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.UserRole;

public record CreateSellerDto(
        String username,
        String nickname,
        String email,
        String password,
        UserRole role,
        String businessNumber,
        String businessName,
        String phoneNumber,
        String address
) {

}
