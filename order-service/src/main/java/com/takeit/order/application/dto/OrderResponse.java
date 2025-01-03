package com.takeit.order.application.dto;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;

public record OrderResponse(
	UUID orderId,
	UUID productId,
	Long quantity,
	Long amount
) {
	public static OrderResponse from(Order order, UUID productId) {
		return new OrderResponse(
			order.getUuid(),
			productId,
			order.getQuantity(),
			order.getAmount()
		);
	}
}
