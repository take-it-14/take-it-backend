package com.takeit.order.presentation.request;

import com.takeit.order.application.dto.OrderStatusUpdateDto;

import jakarta.validation.constraints.NotEmpty;

public record OrderStatusUpdateRequest(
	@NotEmpty
	String status
) {
	public OrderStatusUpdateDto toServiceDto(){
		return new OrderStatusUpdateDto(status);
	}
}
