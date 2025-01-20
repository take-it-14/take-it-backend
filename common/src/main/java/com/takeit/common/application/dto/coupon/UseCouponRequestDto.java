package com.takeit.common.application.dto.coupon;

import java.util.UUID;

public record UseCouponRequestDto(
        UUID userCouponId,
        Long amount
) {
    public static UseCouponRequestDto of(UUID uuid, Long amount) {
        return new UseCouponRequestDto(uuid, amount);
    }
}
