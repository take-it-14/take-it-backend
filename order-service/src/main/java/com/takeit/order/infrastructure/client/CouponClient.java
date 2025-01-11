package com.takeit.order.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "coupon-service")
public interface CouponClient {
	@GetMapping("/feign/v1/coupons/{userCouponId}")
	Long validUserCouponAndGetUserCouponId(
		@PathVariable UUID userCouponId,
		@RequestParam(name = "userId") Long userId
	);
	@GetMapping("/feign/v1/coupons")
	UUID getUserCouponUuid(
		@RequestParam(name = "userCouponId") Long userCouponId
	);
}
