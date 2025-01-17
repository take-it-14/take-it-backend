package com.takeit.common.application.dto;

import java.io.Serializable;
import java.util.UUID;

public record OrderUuidDto(
	UUID orderId
) implements Serializable {
	public static OrderUuidDto from(UUID orderId) {
		return new OrderUuidDto(orderId);
	}
}
