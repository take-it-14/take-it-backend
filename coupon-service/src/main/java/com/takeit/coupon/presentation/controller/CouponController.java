package com.takeit.coupon.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.coupon.application.dto.CreateCouponResponse;
import com.takeit.coupon.application.dto.UpdateCouponResponse;
import com.takeit.coupon.application.service.CouponService;
import com.takeit.coupon.presentation.request.CreateCouponRequest;
import com.takeit.coupon.presentation.request.UpdateCouponRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public CommonResponse<CreateCouponResponse> createCoupon(
            @RequestHeader(name = "X-Username") String username,
            @RequestBody @Valid CreateCouponRequest request
    ) {
        return CommonResponse.ofSuccess("쿠폰 생성에 성공했습니다.", couponService.createCoupon(request, username));
    }

    @PutMapping("/{couponId}")
    public CommonResponse<UpdateCouponResponse> updateCoupon(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID couponId,
            @RequestBody @Valid UpdateCouponRequest request
    ) {
        return CommonResponse.ofSuccess("쿠폰 수정에 성공했습니다.", couponService.updateCoupon(couponId, request, username));
    }
}
