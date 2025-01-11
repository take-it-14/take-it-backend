package com.takeit.review.application.service;

import com.takeit.review.application.dto.product.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto getProductId(UUID productId);

    List<ProductDto> getProducts(List<Long> idList);

    void updateProductStars(Long productId, Double stars);
}
