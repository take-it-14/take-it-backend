package com.takeit.review.application.service;

import com.takeit.review.application.dto.product.ProductDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto getProductId(UUID productId);

    List<ProductDto> getProducts(List<Long> idList);
}
