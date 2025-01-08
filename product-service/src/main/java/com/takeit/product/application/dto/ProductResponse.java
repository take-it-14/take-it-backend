package com.takeit.product.application.dto;

import com.takeit.product.domain.entity.Product;
import java.util.UUID;

public record ProductResponse(
        UUID ProductId
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getUuid());
    }
}
