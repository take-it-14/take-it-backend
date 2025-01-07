package com.takeit.product.application.dto;

import com.takeit.product.domain.entity.Product;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDetailResponse(
        UUID ProductId,
        String productName,
        String description,
        String imageUrl,
        Long price,
        Integer stock,
        Integer limitPerUser,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        Boolean isActive
) {
    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getUuid(),
                product.getProductName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                product.getStock(),
                product.getLimitPerUser(),
                product.getOpenTime(),
                product.getCloseTime(),
                product.getIsActive()
        );
    }
}
