package com.takeit.order.infrastructure.client;

import java.util.UUID;

import com.takeit.common.application.dto.coupon.UseCouponDto;
import com.takeit.common.application.dto.coupon.UseCouponRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "coupon-service")
public interface CouponClient {
	@PostMapping("/feign/v1/coupons/used")
	UseCouponDto validUserCouponAndGetUserCouponId(
			@RequestBody UseCouponRequestDto request,
			@RequestParam(name = "userId") Long userId
	);
	@GetMapping("/feign/v1/coupons")
	UUID getUserCouponUuid(
		@RequestParam(name = "userCouponId") Long userCouponId
	);
}
