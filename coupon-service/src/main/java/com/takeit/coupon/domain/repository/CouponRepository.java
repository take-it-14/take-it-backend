package com.takeit.coupon.domain.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.coupon.application.dto.CouponPageResponse;
import com.takeit.coupon.domain.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {
    Coupon save(Coupon coupon);

    Optional<Coupon> findByUuidAndIsDeletedIsFalse(UUID couponId);

    CouponPageResponse findAll(Predicate predicate, Pageable pageable);
}
