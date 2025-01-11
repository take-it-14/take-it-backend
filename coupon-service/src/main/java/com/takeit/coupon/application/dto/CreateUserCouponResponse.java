package com.takeit.coupon.application.dto;

import com.takeit.coupon.domain.entity.UserCoupon;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserCouponResponse(
        UUID id,
        String couponName,
        String categoryName,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static CreateUserCouponResponse of(UserCoupon userCoupon, String name, String categoryName) {
        return new CreateUserCouponResponse(
                userCoupon.getUuid(),
                name,
                categoryName,
                userCoupon.getStartDate(),
                userCoupon.getEndDate()
        );
    }
}
