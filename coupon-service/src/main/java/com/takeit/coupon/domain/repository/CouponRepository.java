package com.takeit.coupon.domain.repository;

import com.takeit.coupon.domain.entity.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
    Coupon save(Coupon coupon);

    Optional<Coupon> findByUuid(UUID couponId);
}
