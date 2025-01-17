package com.takeit.auth.infrastructure.client;

import com.takeit.auth.application.service.CouponService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "coupon-service")
public interface CouponClient extends CouponService {
    @PostMapping("/feign/v1/coupons/signup")
    boolean createSignupUserCoupon(@RequestBody Long userId);
}
