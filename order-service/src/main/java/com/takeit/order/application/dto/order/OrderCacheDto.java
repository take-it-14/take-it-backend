package com.takeit.order.application.dto.order;

import java.io.Serializable;
import java.util.UUID;

public record OrderCacheDto(
	UUID uuid,
	Long customerId,
	Long productId,
	Long userCouponId,
	Long quantity,
	Long amount
) implements Serializable {
	public static OrderCacheDto of(UUID uuid, Long customerId, Long productId, Long userCouponId, Long quantity, Long amount) {
		return new OrderCacheDto(uuid, customerId, productId, userCouponId, quantity, amount);
	}
}
