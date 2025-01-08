package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Long> {
    boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon);
}
