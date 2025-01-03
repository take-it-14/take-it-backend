package com.takeit.order.application.dto;

public record OrderUpdateDto(
	Long quantity,
	Long amount
) {
}
