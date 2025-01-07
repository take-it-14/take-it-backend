package com.takeit.coupon.application.dto;

import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.type.CouponType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateCouponResponse(
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

    public static CreateCouponResponse of(Coupon coupon, String categoryName) {
        return new CreateCouponResponse(
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
