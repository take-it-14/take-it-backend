package com.takeit.order.application.dto.product;

import java.util.UUID;

public record CancelProduct(
        UUID productId,
        Long quantity
) {
    public static CancelProduct create(UUID productId, Long quantity) {
        return new CancelProduct(productId, quantity);
    }
}
