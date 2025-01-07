package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<Coupon, Long> {
}
