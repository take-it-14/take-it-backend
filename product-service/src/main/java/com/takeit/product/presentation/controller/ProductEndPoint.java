package com.takeit.product.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.product.application.dto.product.ProductEntityResponse;
import com.takeit.product.application.service.ProductRedisService;
import com.takeit.product.application.service.ProductService;
import com.takeit.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/v1/products")
public class ProductEndPoint {
    private final ProductService productService;
    private final ProductRedisService productRedisService;

    @GetMapping("/uuid/{productId}")
    public ProductEntityResponse getProductByUuid(@PathVariable UUID productId) {
        return productService.getProductEntity(productId);
    }

    @GetMapping
    public List<ProductEntityResponse> getAllProducts(@RequestParam(required = false) List<Long> idList,
                                                      @QuerydslPredicate(root = Product.class) Predicate predicate) {
        return productService.getProductEntities(idList, predicate);
    }

    @PostMapping("/{productId}")
    public void updateProductStars(@RequestBody Double stars, @PathVariable Long productId) {
        productService.updateProductStars(productId, stars);
    }

    @PostMapping("/{productId}/occupy")
    public void occupyProduct(@PathVariable UUID productId,
                              @RequestParam(required = false) int quantity) {
        productRedisService.occupyProduct(productId, quantity);
    }
}
