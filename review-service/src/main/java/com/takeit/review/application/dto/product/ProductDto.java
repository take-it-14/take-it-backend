package com.takeit.review.application.dto.product;

import java.util.UUID;

public record ProductDto(
        Long id,
        UUID uuid,
        String productName
) {
}
