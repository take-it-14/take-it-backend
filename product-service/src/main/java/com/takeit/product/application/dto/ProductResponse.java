package com.takeit.product.application.dto;

import com.takeit.product.domain.entity.Product;

public record ProductResponse(
        String ProductId
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getUuid().toString());
    }
}
