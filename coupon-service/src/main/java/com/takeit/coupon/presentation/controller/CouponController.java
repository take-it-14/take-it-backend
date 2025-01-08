package com.takeit.coupon.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.coupon.application.dto.CouponPageResponse;
import com.takeit.coupon.application.dto.CouponResponse;
import com.takeit.coupon.application.dto.CreateCouponResponse;
import com.takeit.coupon.application.dto.UpdateCouponResponse;
import com.takeit.coupon.application.service.CouponService;
import com.takeit.coupon.domain.entity.Coupon;
import com.takeit.coupon.presentation.request.CreateCouponRequest;
import com.takeit.coupon.presentation.request.UpdateCouponRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

    @DeleteMapping("/{couponId}")
    public CommonResponse<?> deleteCoupon(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID couponId
    ) {
        couponService.deleteCoupon(couponId, username);

        return CommonResponse.ofSuccess("쿠폰 삭제에 성공했습니다.", null);
    }

    @GetMapping("/{couponId}")
    public CommonResponse<CouponResponse> getCoupon(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID couponId
    ) {
        return CommonResponse.ofSuccess("쿠폰 상세 조회에 성공했습니다.", couponService.getCoupon(username, couponId));
    }

    @GetMapping
    public CommonResponse<CouponPageResponse> getCoupons(
            @RequestHeader(name = "X-Username") String username,
            @QuerydslPredicate(root = Coupon.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable
    ) {
        return CommonResponse.ofSuccess("쿠폰 상세 조회에 성공했습니다.", couponService.getCoupons(username, predicate, pageable));
    }
}
