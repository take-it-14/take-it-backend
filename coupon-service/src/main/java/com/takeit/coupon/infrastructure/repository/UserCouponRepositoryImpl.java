package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final JpaUserCouponRepository jpaUserCouponRepository;

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return jpaUserCouponRepository.save(userCoupon);
    }

    @Override
    public Optional<UserCoupon> findByUuidAndUserIdAndIsDeletedIsFalse(UUID userCouponId, Long userId) {
        return jpaUserCouponRepository.findByUuidAndUserIdAndIsDeletedIsFalse(userCouponId, userId);
    }
}
