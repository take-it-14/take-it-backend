package com.takeit.product.application.dto.product;

import com.takeit.product.domain.entity.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductEntityResponse(
        Long id,
        UUID ProductId,
        String productName,
        String description,
        Long price,
        Integer stock,
        Integer limitPerUser,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        Boolean isActive
) {
    public static ProductEntityResponse from(Product product) {
        return new ProductEntityResponse(
                product.getId(),
                product.getUuid(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getLimitPerUser(),
                product.getOpenTime(),
                product.getCloseTime(),
                product.getIsActive());
    }
}
