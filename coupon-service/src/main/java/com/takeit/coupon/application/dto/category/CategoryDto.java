package com.takeit.coupon.application.dto.category;

import java.util.UUID;

public record CategoryDto(
        Long id,
        UUID uuid,
        String name
) {
}
