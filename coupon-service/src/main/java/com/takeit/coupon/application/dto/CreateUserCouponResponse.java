package com.takeit.coupon.application.dto;

import com.takeit.coupon.domain.entity.UserCoupon;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserCouponResponse(
        UUID id,
        String couponName,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static CreateUserCouponResponse of(UserCoupon userCoupon, String name) {
        return new CreateUserCouponResponse(
                userCoupon.getUuid(),
                name,
                userCoupon.getStartDate(),
                userCoupon.getEndDate()
        );
    }
}
