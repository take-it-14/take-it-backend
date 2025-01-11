package com.takeit.review.infrastructure.client;

import com.takeit.review.application.dto.product.ProductDto;
import com.takeit.review.application.service.ProductService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient extends ProductService {
    @GetMapping("/feign/v1/products/{productId}")
    ProductDto getProductId(@PathVariable UUID productId);

    @GetMapping("/feign/v1/products")
    List<ProductDto> getProducts(@RequestParam(name = "idList") List<Long> idList);
}
