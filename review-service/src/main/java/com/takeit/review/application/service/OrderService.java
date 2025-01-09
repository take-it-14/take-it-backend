package com.takeit.review.application.service;

import java.util.UUID;

public interface OrderService {
    Long getProductId(UUID orderId, Long userId);
}
