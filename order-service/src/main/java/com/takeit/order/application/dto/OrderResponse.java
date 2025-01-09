package com.takeit.order.application.dto;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;

public record OrderResponse(
	UUID orderId,
	UUID productId,
	UUID userCouponId,
	Long quantity,
	Long amount
) {
	public static OrderResponse of(Order order, UUID productId, UUID userCouponId) {
		return new OrderResponse(
			order.getUuid(),
			productId,
			userCouponId,
			order.getQuantity(),
			order.getAmount()
		);
	}
}
