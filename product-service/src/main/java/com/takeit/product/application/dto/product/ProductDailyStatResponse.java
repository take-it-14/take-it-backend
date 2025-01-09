package com.takeit.product.application.dto.product;

import com.takeit.product.domain.entity.ProductDailyStat;

import java.util.Date;
import java.util.UUID;

public record ProductDailyStatResponse(
	UUID productId,
	Date baseDate,
	Long soldStock,
	Long stock,
	Long people
) {
	public static ProductDailyStatResponse of(ProductDailyStat productDailyStat, UUID productId) {
		return new ProductDailyStatResponse(
			productId,
			productDailyStat.getBaseDate(),
			productDailyStat.getSoldStock(),
			productDailyStat.getStock(),
			productDailyStat.getPeople()
		);
	}
}
