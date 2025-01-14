package com.takeit.coupon.domain.repository;

import com.querydsl.core.types.Predicate;
import com.takeit.coupon.application.dto.UserCouponPageResponse;
import com.takeit.coupon.application.dto.user.UserDto;
import com.takeit.coupon.domain.entity.UserCoupon;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import com.takeit.coupon.domain.entity.Coupon;

public interface UserCouponRepository {
    UserCoupon save(UserCoupon userCoupon);

    Optional<UserCoupon> findByUuidAndIsDeletedIsFalse(UUID userCouponId);

    boolean existsByCouponAndIsDeletedIsFalse(Coupon coupon);

    Optional<UserCoupon> findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(UUID userCouponId);

    UserCouponPageResponse findAll(Predicate predicate, Pageable pageable, UserDto userId);

    Optional<UserCoupon> findByIdAndIsDeletedIsFalse(Long userCouponId);

    Optional<UserCoupon> findById(Long userCouponId);
}
