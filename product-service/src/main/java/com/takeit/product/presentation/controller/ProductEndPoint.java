package com.takeit.product.presentation.controller;

import com.querydsl.core.types.Predicate;
import com.takeit.product.application.dto.product.ProductEntityResponse;
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

    @GetMapping("/uuid/{productId}")
    public ProductEntityResponse getProductByUuid(@PathVariable UUID productId) {
        return productService.getProductEntity(productId);
    }

    @GetMapping
    public List<ProductEntityResponse> getAllProducts(@RequestParam(required = false) List<Long> idList,
                                                      @QuerydslPredicate(root = Product.class) Predicate predicate) {
        return productService.getProductEntities(idList, predicate);
    }
}
