package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaUserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUuidAndIsDeletedIsFalse(UUID userCouponId);
    boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon);
}
