package com.takeit.product.application.dto;

import java.time.LocalDateTime;

public record UpdateProductDto(
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

}
