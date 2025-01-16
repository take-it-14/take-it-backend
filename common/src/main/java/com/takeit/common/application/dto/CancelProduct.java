package com.takeit.common.application.dto;

import java.io.Serializable;
import java.util.UUID;

public record CancelProduct(
	UUID productId,
	Long quantity
) implements Serializable {
	public static CancelProduct create(UUID productId, Long quantity) {
		return new CancelProduct(productId, quantity);
	}
}