package com.takeit.review.application.service;

import com.takeit.review.application.dto.order.OrderDto;

import java.util.UUID;

public interface OrderService {
    OrderDto getOrder(UUID orderId, Long userId);
}
