package com.takeit.coupon.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.takeit.coupon.domain.type.CouponType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponPageResponse(
        CouponPage couponPage
) {

    public static CouponPageResponse from(Page<CouponPage.Coupon> coupons) {
        return new CouponPageResponse(
                new CouponPage(coupons)
        );
    }

    public static class CouponPage extends PagedModel<CouponPage.Coupon> {
        public CouponPage(Page<Coupon> page) {
            super(new PageImpl<>(
                    page.getContent(),
                    page.getPageable(),
                    page.getTotalElements()
            ));
        }

        @Getter
        @Builder
        public static class Coupon {
            private UUID id;
            private String name;
            private CouponType type;
            private int discountValue;
            private int minAmount;
            private Long categoryId;
            private LocalDateTime startDate;
            private LocalDateTime endDate;
            private int expirationDate;

            @QueryProjection
            public Coupon(UUID id, String name, CouponType type, int discountValue, int minAmount, Long categoryId, LocalDateTime startDate, LocalDateTime endDate, int expirationDate) {
                this.id = id;
                this.name = name;
                this.type = type;
                this.discountValue = discountValue;
                this.minAmount = minAmount;
                this.categoryId = categoryId;
                this.startDate = startDate;
                this.endDate = endDate;
                this.expirationDate = expirationDate;
            }

            public static Coupon from(com.takeit.coupon.domain.entity.Coupon coupon) {
                return Coupon.builder()
                        .id(coupon.getUuid())
                        .name(coupon.getName())
                        .type(coupon.getType())
                        .discountValue(coupon.getDiscountValue())
                        .minAmount(coupon.getMinAmount())
                        .categoryId(coupon.getCategoryId())
                        .startDate(coupon.getStartDate())
                        .endDate(coupon.getEndDate())
                        .expirationDate(coupon.getExpirationDate())
                        .build();
            }

        }
    }
}
