package com.takeit.product.presentation.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.product.application.dto.ProductDailyStatResponse;
import com.takeit.product.application.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/board/{productId}")
	public CommonResponse<Page<ProductDailyStatResponse>> getProductDailyStat(
		@PathVariable UUID productId,
		@RequestParam String startDate,
		@RequestParam String endDate,
		Pageable pageable
	) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);

			return CommonResponse.ofSuccess("상품 통계 조회",
				productService.getProductDailyStat(productId, start, end, pageable));
		} catch (ParseException e) {
			throw new CustomException(ErrorCode.INVALID_DATE_FORMAT);
		}
	}
}
