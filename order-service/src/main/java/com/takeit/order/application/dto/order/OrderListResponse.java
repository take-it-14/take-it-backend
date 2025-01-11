package com.takeit.order.application.dto.order;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;

public record OrderListResponse(
	UUID orderId,
	UUID productId,
	OrderStatus status
) {
	public static OrderListResponse of(Order order, UUID productId) {
		return new OrderListResponse(
			order.getUuid(),
			productId,
			order.getStatus()
		);
	}
}
