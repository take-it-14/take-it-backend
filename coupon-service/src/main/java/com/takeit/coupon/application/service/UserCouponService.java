package com.takeit.coupon.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.coupon.application.dto.CreateUserCouponResponse;
import com.takeit.coupon.application.dto.UpdateUserCouponResponse;
import com.takeit.coupon.application.dto.UserCouponResponse;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import com.takeit.coupon.presentation.request.CreateUserCouponRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.takeit.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public CreateUserCouponResponse create(CreateUserCouponRequest request, String username) {
        // todo : user 권한 체크
        Long userId = 1L;
        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(request.couponId()).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        if(coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(COUPON_EXPIRED);
        }

        return CreateUserCouponResponse.of(userCouponRepository.save(UserCoupon.create(coupon, userId)), coupon.getName());
    }

    @Transactional
    public void delete(UUID userCouponId, String username) {
        // todo : user 권한 체크
        Long userId = 1L;

        UserCoupon coupon = userCouponRepository.findByUuidAndUserIdAndIsDeletedIsFalse(userCouponId, userId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));
        coupon.delete(username);
    }

    @Transactional
    public UpdateUserCouponResponse used(UUID userCouponId, String username) {
        // todo : user 권한 체크
        Long userId = 1L;

        UserCoupon coupon = userCouponRepository.findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        // todo : user 권한이 master, manager 인 경우 validation x
        validationUserId(coupon, userId);

        if(LocalDateTime.now().isBefore(coupon.getStartDate()) || coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(USER_COUPON_INVALID_DATE_RANGE);
        }

        if(coupon.getIsUsed()) {
            throw new CustomException(USER_COUPON_ALREADY_USED);
        }

        String couponName = coupon.getCoupon().getName();
        coupon.used();

        return UpdateUserCouponResponse.of(userCouponRepository.save(coupon), couponName);
    }

    @Transactional
    public UpdateUserCouponResponse cancel(UUID userCouponId, String username) {
        // todo : user 권한 체크
        Long userId = 1L;

        UserCoupon coupon = userCouponRepository.findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        if(coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(USER_COUPON_EXPIRED);
        }

        if(!coupon.getIsUsed()) {
            throw new CustomException(USER_COUPON_NOT_USED);
        }

        String couponName = coupon.getCoupon().getName();
        coupon.cancel();

        return UpdateUserCouponResponse.of(userCouponRepository.save(coupon), couponName);
    }

    @Transactional(readOnly = true)
    public UserCouponResponse getUserCoupon(UUID userCouponId, String username) {
        // todo : user 권한 체크
        Long userId = 1L;
        UserCoupon userCoupon = userCouponRepository.findByUuidAndUserIdAndFetchJoinCouponAndIsDeletedIsFalse(userCouponId).orElseThrow(() -> new CustomException(USER_COUPON_NOT_FOUND));

        // todo : user 권한이 master, manager 인 경우 validation x
        validationUserId(userCoupon, userId);

        return UserCouponResponse.from(userCoupon);

    }

    private void validationUserId(UserCoupon userCoupon, Long userId) {
        if(!userCoupon.getUserId().equals(userId)) {
            throw new CustomException(UNAUTHORIZED);
        }
    }
}
