package com.takeit.coupon.application.dto.user;

import java.util.UUID;

public record UserDto (
        Long id,
        UUID uuid,
        String role
){
}
