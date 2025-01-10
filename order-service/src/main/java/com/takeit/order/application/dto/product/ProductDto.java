package com.takeit.order.application.dto.product;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDto(
	Long id,
	UUID uuid,
	Long sellerId,
	Long categoryId,
	String productName,
	String description,
	Long price,
	Integer stock,
	Integer limitPerUser,
	LocalDateTime openTime,
	LocalDateTime closeTime,
	Boolean isActive
) {
}
