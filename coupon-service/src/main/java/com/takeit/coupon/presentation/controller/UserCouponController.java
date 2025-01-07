package com.takeit.coupon.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.coupon.application.dto.CreateUserCouponResponse;
import com.takeit.coupon.application.dto.UpdateUserCouponResponse;
import com.takeit.coupon.application.dto.UserCouponResponse;
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

    @PatchMapping("/{userCouponId}/used")
    public CommonResponse<UpdateUserCouponResponse> userCouponUsed(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID userCouponId) {
        return CommonResponse.ofSuccess("사용자 쿠폰을 사용하였습니다.", userCouponService.used(userCouponId, username));
    }

    @PatchMapping("/{userCouponId}/cancel")
    public CommonResponse<UpdateUserCouponResponse> userCouponCancel(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID userCouponId) {
        return CommonResponse.ofSuccess("사용자 쿠폰 사용을 취소하였습니다.", userCouponService.cancel(userCouponId, username));
    }

    @GetMapping("/{userCouponId}")
    public CommonResponse<UserCouponResponse> getUserCoupon(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID userCouponId
    ) {
        return CommonResponse.ofSuccess("사용자 쿠폰 상세 조회에 성공했습니다.", userCouponService.getUserCoupon(userCouponId, username));
    }

}
