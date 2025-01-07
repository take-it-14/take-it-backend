package com.takeit.coupon.domain.repository;

import com.takeit.coupon.domain.entity.UserCoupon;

import java.util.Optional;
import java.util.UUID;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findByUuidAndUserIdAndIsDeletedIsFalse(UUID userCouponId, Long userId);

    Optional<UserCoupon> findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(UUID userCouponId);
}
