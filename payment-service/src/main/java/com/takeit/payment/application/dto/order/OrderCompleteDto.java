package com.takeit.payment.application.dto.order;

import java.io.Serializable;
import java.util.UUID;

public record OrderCompleteDto(
	UUID orderId
) implements Serializable {
	public static OrderCompleteDto from(UUID orderId) {
		return new OrderCompleteDto(orderId);
	}
}
