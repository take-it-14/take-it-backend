package com.takeit.coupon.presentation.request;

import java.util.UUID;

public record CreateUserCouponRequest(
        UUID couponId,
        String username
) {
}
