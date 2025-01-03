package com.takeit.order.application.dto;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;

public record OrderDetailResponse(
	UUID orderId,
	UUID productId,
	Long quantity,
	Long amount,
	OrderStatus status
) {
	public static OrderDetailResponse from(Order order, UUID productId) {
		return new OrderDetailResponse(
			order.getUuid(),
			productId,
			order.getQuantity(),
			order.getAmount(),
			order.getStatus()
		);
	}
}
