package com.takeit.product.presentation.controller;

import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.product.application.dto.ProductResponse;
import com.takeit.product.application.service.ProductService;
import com.takeit.product.presentation.request.CreateProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 등록 성공", productService.createProduct(request.toDto())));
    }
}
