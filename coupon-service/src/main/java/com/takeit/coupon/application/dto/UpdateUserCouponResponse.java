package com.takeit.coupon.application.dto;

import com.takeit.coupon.domain.entity.UserCoupon;

import java.util.UUID;

public record UpdateUserCouponResponse(
        UUID id,
        String couponName,
        boolean isUsed
){
    public static UpdateUserCouponResponse of(UserCoupon userCoupon, String couponName) {
        return new UpdateUserCouponResponse(
                userCoupon.getUuid(),
                couponName,
                userCoupon.getIsUsed()
        );
    }
}
