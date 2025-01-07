package com.takeit.order.presentation.request;

import com.takeit.order.application.dto.OrderUpdateDto;

import jakarta.validation.constraints.NotNull;

public record OrderUpdateRequest(
	@NotNull
	Long quantity,

	@NotNull
	Long amount
) {
	public OrderUpdateDto toServiceDto(){
		return new OrderUpdateDto(quantity, amount);
	}
}
