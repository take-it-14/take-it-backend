package com.takeit.order.infrastructure.client;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.takeit.order.application.dto.ProductDto;

@FeignClient(name = "product-service")
public interface ProductClient {
	@GetMapping("/feign/v1/products/uuid/{productId}")
	ProductDto getProductByUuid(@PathVariable UUID productId);

	@GetMapping("/feign/v1/products")
	List<ProductDto> getAllProducts(@RequestParam(name = "idList") List<Long> idList);

}
