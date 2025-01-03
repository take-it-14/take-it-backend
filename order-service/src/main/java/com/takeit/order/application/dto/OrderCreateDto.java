package com.takeit.order.application.dto;

import java.util.UUID;

public record OrderCreateDto(
	UUID productId,
	Long quantity,
	Long amount
) {
}
