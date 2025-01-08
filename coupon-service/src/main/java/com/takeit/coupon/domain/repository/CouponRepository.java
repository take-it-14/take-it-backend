package com.takeit.coupon.domain.repository;

import com.takeit.coupon.domain.entity.Coupon;

public interface CouponRepository {
    Coupon save(Coupon coupon);
}
