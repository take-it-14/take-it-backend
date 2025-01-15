package com.takeit.coupon.presentation.controller;

import com.takeit.coupon.application.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/coupons")
public class UserCouponEndPoint {
    private final UserCouponService userCouponService;

    @GetMapping("/{userCouponId}")
    public Long validUserCouponAndGetUserCouponId(
            @PathVariable UUID userCouponId,
            @RequestParam(name = "userId") Long userId
    ) {
        return userCouponService.validUserCouponAndUsedAndGetUserCouponId(userCouponId, userId);
    }

    @GetMapping
    public UUID getUserCouponUuid(
            @RequestParam(name = "userCouponId") Long userCouponId
    ) {
        return userCouponService.getUserCouponUuid(userCouponId);
    }
}
