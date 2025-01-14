package com.takeit.order.application.dto.order;

import java.util.UUID;

public record OrderCreateResponse(
	UUID orderId
) {
	public static OrderCreateResponse from(UUID orderId) {
		return new OrderCreateResponse(orderId);
	}
}
