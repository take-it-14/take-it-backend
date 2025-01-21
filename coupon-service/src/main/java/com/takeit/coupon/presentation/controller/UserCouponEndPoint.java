package com.takeit.coupon.presentation.controller;

import com.takeit.common.application.dto.coupon.UseCouponDto;
import com.takeit.common.application.dto.coupon.UseCouponRequestDto;
import com.takeit.coupon.application.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/coupons")
public class UserCouponEndPoint {
    private final UserCouponService userCouponService;

    @PostMapping("/used")
    public UseCouponDto validUserCouponAndGetUserCouponId(
            @RequestBody UseCouponRequestDto request,
            @RequestParam(name = "userId") Long userId
    ) {
        return userCouponService.validUserCouponAndUsedAndGetUserCouponId(request, userId);
    }

    @GetMapping
    public UUID getUserCouponUuid(
            @RequestParam(name = "userCouponId") Long userCouponId
    ) {
        return userCouponService.getUserCouponUuid(userCouponId);
    }

    @PostMapping("/signup")
    public boolean signupUserCoupon(
            @RequestParam Long userId
    ) {
        return userCouponService.createSignupUserCoupon(userId);
    }

}
