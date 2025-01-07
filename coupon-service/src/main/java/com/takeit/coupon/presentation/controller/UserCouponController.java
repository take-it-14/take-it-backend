package com.takeit.coupon.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.coupon.application.dto.CreateUserCouponResponse;
import com.takeit.coupon.application.service.UserCouponService;
import com.takeit.coupon.presentation.request.CreateUserCouponRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-coupons")
public class UserCouponController {
    private final UserCouponService userCouponService;

    @PostMapping
    public CommonResponse<CreateUserCouponResponse> createUserCoupon(
            @RequestHeader(name = "X-Username") String username,
            @RequestBody CreateUserCouponRequest request
    ) {
        return CommonResponse.ofSuccess("사용자 쿠폰 발급에 성공했습니다.", userCouponService.create(request, username));
    }

    @DeleteMapping("/{userCouponId}")
    public CommonResponse<?> deleteUserCoupon(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID userCouponId) {
        userCouponService.delete(userCouponId, username);
        return CommonResponse.ofSuccess("사용자 쿠폰 삭제에 성공했습니다.", null);
    }
}
