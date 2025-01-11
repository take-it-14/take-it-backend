package com.takeit.order.application.dto.order;

public record OrderUpdateDto(
	Long quantity,
	Long amount
) {
}
