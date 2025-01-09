package com.takeit.product.presentation.controller;

import com.takeit.product.application.dto.ProductEntityResponse;
import com.takeit.product.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/products")
public class ProductEndPoint {
    private final ProductService productService;

    @GetMapping("/uuid/{productId}")
    public ProductEntityResponse getProductByUuid(@PathVariable UUID productId) {
        return productService.getProductEntity(productId);
    }
}
