package com.takeit.coupon.domain.repository;

import com.takeit.coupon.domain.entity.UserCoupon;

import com.takeit.coupon.domain.entity.Coupon;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);
    boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon);
}
