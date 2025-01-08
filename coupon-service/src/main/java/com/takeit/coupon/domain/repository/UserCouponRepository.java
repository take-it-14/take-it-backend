package com.takeit.coupon.domain.repository;

import com.takeit.coupon.domain.entity.UserCoupon;

import java.util.Optional;
import java.util.UUID;

import com.takeit.coupon.domain.entity.Coupon;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(UUID userCouponId, Long userId);

    Optional<UserCoupon> findByUuidAndIsDeletedIsFalse(UUID userCouponId);

    boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon);
}
