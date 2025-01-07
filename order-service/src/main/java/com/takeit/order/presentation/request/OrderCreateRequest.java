package com.takeit.order.presentation.request;

import java.util.UUID;

import com.takeit.order.application.dto.OrderCreateDto;

import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
	@NotNull
	UUID productId,

	@NotNull
	Long quantity,

	@NotNull
	Long amount
) {
	public OrderCreateDto toServiceDto(){
		return new OrderCreateDto(productId, quantity, amount);
	}
}
