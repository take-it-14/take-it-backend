package com.takeit.favorite.application.service;

import com.takeit.favorite.application.dto.product.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDto getProduct(UUID uuid);

    List<ProductDto> getProducts(List<Long> idList);
}
