package com.takeit.common.application.dto.coupon;

public record UseCouponDto(
        Long userCouponId,
        Long discountAmount
) {
    public static UseCouponDto of(Long userCouponId, Long discountAmount) {
        return new UseCouponDto(userCouponId, discountAmount);
    }
}
