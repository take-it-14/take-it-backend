package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final JpaCouponRepository jpaCouponRepository;

    @Override
    public Coupon save(Coupon coupon) {
        return jpaCouponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findByUuidAndIsDeletedIsFalse(UUID couponId) {
        return jpaCouponRepository.findByUuidAndIsDeletedIsFalse(couponId);
    }
}
