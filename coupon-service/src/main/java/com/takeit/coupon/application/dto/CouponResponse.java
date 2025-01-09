package com.takeit.coupon.application.dto;

import com.takeit.coupon.domain.entity.Coupon;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String name,
        String type,
        int discountValue,
        int minAmount,
        String categoryName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int expirationDate
) {
    public static CouponResponse of(Coupon coupon, String categoryName) {
        return new CouponResponse(
                coupon.getUuid(),
                coupon.getName(),
                coupon.getType().toString(),
                coupon.getDiscountValue(),
                coupon.getMinAmount(),
                categoryName,
                coupon.getStartDate(),
                coupon.getEndDate(),
                coupon.getExpirationDate()
        );
    }
}
