package com.takeit.coupon.application.dto;

import com.takeit.coupon.domain.entity.UserCoupon;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCouponResponse(
        UUID id,
        String couponName,
        String type,
        int discountValue,
        int minAmount,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isUsed
) {
    public static UserCouponResponse from(UserCoupon userCoupon) {
        return new UserCouponResponse(
                userCoupon.getUuid(),
                userCoupon.getCoupon().getName(),
                userCoupon.getCoupon().getType().toString(),
                userCoupon.getCoupon().getDiscountValue(),
                userCoupon.getCoupon().getMinAmount(),
                userCoupon.getStartDate(),
                userCoupon.getEndDate(),
                userCoupon.getIsUsed()
        );
    }
}
