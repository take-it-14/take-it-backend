package com.takeit.coupon.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.coupon.application.dto.CreateUserCouponResponse;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.domain.entity.UserCoupon;
import com.takeit.coupon.domain.repository.CouponRepository;
import com.takeit.coupon.domain.repository.UserCouponRepository;
import com.takeit.coupon.presentation.request.CreateUserCouponRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.takeit.common.exception.ErrorCode.COUPON_EXPIRED;
import static com.takeit.common.exception.ErrorCode.COUPON_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public CreateUserCouponResponse create(CreateUserCouponRequest request, String username) {
        // todo : user 권한 체크
        Long userId = 1L;
        Coupon coupon = couponRepository.findByUuidAndIsDeletedIsFalse(request.couponId()).orElseThrow(() -> new CustomException(COUPON_NOT_FOUND));

        if(coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(COUPON_EXPIRED);
        }

        return CreateUserCouponResponse.of(userCouponRepository.save(UserCoupon.create(coupon, userId)), coupon.getName());
    }
}
