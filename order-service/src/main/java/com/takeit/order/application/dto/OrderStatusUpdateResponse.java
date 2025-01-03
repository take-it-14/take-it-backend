package com.takeit.order.application.dto;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;

public record OrderStatusUpdateResponse(
	UUID orderId,
	OrderStatus status
) {
	public static OrderStatusUpdateResponse from(Order order) {
		return new OrderStatusUpdateResponse(
			order.getUuid(),
			order.getStatus()
		);
	}
}
