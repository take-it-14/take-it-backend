package com.takeit.order.application.dto;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;

public record OrderCreateResponse(
	UUID orderId,
	UUID productId,
	Long quantity,
	Long amount
) {
	public static OrderCreateResponse from(Order order, UUID productId) {
		return new OrderCreateResponse(
			order.getUuid(),
			productId,
			order.getQuantity(),
			order.getAmount()
		);
	}
}
