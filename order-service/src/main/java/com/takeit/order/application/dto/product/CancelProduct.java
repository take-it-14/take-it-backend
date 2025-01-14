package com.takeit.order.application.dto.product;

public record CancelProduct(
        Long productId,
        Long quantity
) {
    public static CancelProduct create(Long productId, Long quantity) {
        return new CancelProduct(productId, quantity);
    }
}
