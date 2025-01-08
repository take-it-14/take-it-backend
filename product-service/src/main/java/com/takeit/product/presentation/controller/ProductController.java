package com.takeit.product.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.common.presentation.dto.CommonResponse;
import com.takeit.product.application.dto.ProductDetailResponse;
import com.takeit.product.application.dto.ProductPageResponse;
import com.takeit.product.application.dto.ProductResponse;
import com.takeit.product.application.service.ProductService;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.presentation.request.CreateProductRequest;
import com.takeit.product.presentation.request.UpdateProductRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(@Valid CreateProductRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 등록 성공", productService.createProduct(request.toDto())));
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(
            @RequestHeader(name = "X-Username") String username,
            @PathVariable UUID productId,
            @Valid UpdateProductRequest request) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 수정 성공", productService.updateProduct(productId, request.toDto(), username)));
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductResponse>> deleteProduct(
            @PathVariable UUID productId,
            @RequestHeader(value = "X-Username") String username) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 삭제 성공", productService.deleteProduct(productId, username)));
    }

    // 상품 단건 조회
    @GetMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductDetailResponse>> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 조회 성공", productService.getProduct(productId)));
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse<ProductPageResponse>> getProducts(
            @QuerydslPredicate(root = Product.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok().body(CommonResponse.ofSuccess("상품 목록 조회 성공", productService.getProducts(predicate, pageable)));
    }
}
