package com.takeit.coupon.infrastructure.repository;

import com.takeit.coupon.domain.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCouponRepositoryImpl implements UserCouponRepository {
    private final JpaUserCouponRepository jpaUserCouponRepository;
}
