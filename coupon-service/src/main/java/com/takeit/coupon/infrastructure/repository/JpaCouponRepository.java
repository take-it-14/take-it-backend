package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByUuidAndIsDeletedIsFalse(UUID couponId);

    Optional<Coupon> findByNameAndIsDeletedIsFalse(String couponName);
}
