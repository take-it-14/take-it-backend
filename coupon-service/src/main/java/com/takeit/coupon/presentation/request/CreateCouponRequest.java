package com.takeit.coupon.presentation.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.takeit.coupon.domain.type.CouponType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateCouponRequest(
        @Length(max = 50, min = 5)
        @NotNull
        String name,
        @NotNull
        CouponType type,
        @Min(1)
        @NotNull
        @JsonProperty("discount_value")
        Integer discountValue,
        @JsonProperty("min_amount")
        Integer minAmount,
        @JsonProperty("category_id")
        UUID categoryId,
        @NotNull
        @JsonProperty("start_date")
        LocalDateTime startDate,
        @NotNull
        @JsonProperty("end_date")
        LocalDateTime endDate,
        @JsonProperty("expiration_date")
        Integer expirationDate
) {
}
