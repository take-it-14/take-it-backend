package com.takeit.order.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
	COMPLETED,
	DELIVERED,
	CANCELLED;

	public static OrderStatus of(String request){
		if(request==null) return null;
		return switch (request){
			case "COMPLETED" -> COMPLETED;
			case "DELIVERED" -> DELIVERED;
			case "CANCELLED" -> CANCELLED;
			default -> null;
		};
	}
}