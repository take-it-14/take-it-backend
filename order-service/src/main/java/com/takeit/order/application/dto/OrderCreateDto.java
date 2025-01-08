package com.takeit.order.application.dto;

import java.util.UUID;

public record OrderCreateDto(
	UUID productId,
	UUID userCouponId,
	Long quantity,
	Long amount
) {
}
