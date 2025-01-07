package com.takeit.product.application.service;

import java.util.Date;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.takeit.product.application.dto.ProductDailyStatResponse;
import com.takeit.product.domain.entity.ProductDailyStat;
import com.takeit.product.domain.repository.ProductDailyStatRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductDailyStatRepository productDailyStatRepository;

	public Page<ProductDailyStatResponse> getProductDailyStat(UUID productId, Date startDate, Date endDate, Pageable pageable) {
		// TODO: product UUID->ID 변환 요청
		Long newProductId=1L;

		Page<ProductDailyStat> productDailyStats = productDailyStatRepository.findByProductIdAndBaseDateBetween(newProductId, startDate, endDate, pageable);

		return productDailyStats.map(
			productDailyStat -> ProductDailyStatResponse.of(productDailyStat, productId)
		);
	}
}
