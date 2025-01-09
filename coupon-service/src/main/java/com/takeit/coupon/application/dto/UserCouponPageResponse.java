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

public record UserCouponPageResponse(
        UserCouponPage userCouponPage
) {
    public static UserCouponPageResponse from(Page<UserCouponPageResponse.UserCouponPage.UserCoupon> coupons) {
        return new UserCouponPageResponse(
                new UserCouponPage(coupons)
        );
    }

    public static class UserCouponPage extends PagedModel<UserCouponPageResponse.UserCouponPage.UserCoupon> {
        public UserCouponPage(Page<UserCouponPageResponse.UserCouponPage.UserCoupon> page) {
            super(new PageImpl<>(
                    page.getContent(),
                    page.getPageable(),
                    page.getTotalElements()
            ));
        }

        @Getter
        @Builder
        public static class UserCoupon {
            private UUID id;
            private String couponName;
            private String username;
            private CouponType type;
            private int discountValue;
            private int minAmount;
            private Long categoryId;
            private LocalDateTime startDate;
            private LocalDateTime endDate;

            @QueryProjection
            public UserCoupon(UUID id, String name, String username, CouponType type, int discountValue, int minAmount, Long categoryId, LocalDateTime startDate, LocalDateTime endDate) {
                this.id = id;
                this.couponName = name;
                this.username = username;
                this.type = type;
                this.discountValue = discountValue;
                this.minAmount = minAmount;
                this.categoryId = categoryId;
                this.startDate = startDate;
                this.endDate = endDate;
            }

            public static UserCouponPageResponse.UserCouponPage.UserCoupon from(com.takeit.coupon.domain.entity.UserCoupon coupon) {
                return UserCoupon.builder()
                        .id(coupon.getUuid())
                        .couponName(coupon.getCoupon().getName())
                        .username(coupon.getCreatedBy())
                        .type(coupon.getCoupon().getType())
                        .discountValue(coupon.getCoupon().getDiscountValue())
                        .minAmount(coupon.getCoupon().getMinAmount())
                        .categoryId(coupon.getCoupon().getCategoryId())
                        .startDate(coupon.getStartDate())
                        .endDate(coupon.getEndDate())
                        .build();
            }

        }
    }
}
