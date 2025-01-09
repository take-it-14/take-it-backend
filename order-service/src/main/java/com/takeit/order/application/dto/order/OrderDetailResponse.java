package com.takeit.order.application.dto.order;

import java.util.UUID;

import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.enums.OrderStatus;

public record OrderDetailResponse(
	UUID orderId,
	UUID productId,
	UUID userCouponId,
	Long quantity,
	Long amount,
	OrderStatus status
) {
	public static OrderDetailResponse of(Order order, UUID productId, UUID userCouponId) {
		return new OrderDetailResponse(
			order.getUuid(),
			productId,
			userCouponId,
			order.getQuantity(),
			order.getAmount(),
			order.getStatus()
		);
	}
}
