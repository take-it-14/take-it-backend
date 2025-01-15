package com.takeit.product.application.dto.product;

import java.util.UUID;

public record CancelProduct(
        UUID productId,
        Long quantity
)  {}

