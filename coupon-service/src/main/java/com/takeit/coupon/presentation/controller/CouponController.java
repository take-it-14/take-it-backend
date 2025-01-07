package com.takeit.coupon.presentation.controller;

import com.takeit.coupon.application.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
}
