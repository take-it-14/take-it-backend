package com.takeit.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
	PENDING,
	COMPLETED,
	DELIVERED,
	CANCELLED;

	public static OrderStatus of(String request){
		return switch (request){
			case "PENDING" -> PENDING;
			case "COMPLETED" -> COMPLETED;
			case "DELIVERED" -> DELIVERED;
			case "CANCELLED" -> CANCELLED;
			default -> null;
		};
	}
}