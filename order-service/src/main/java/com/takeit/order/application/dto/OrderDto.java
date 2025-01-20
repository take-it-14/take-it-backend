package com.takeit.order.application.dto;

import com.takeit.order.domain.entity.Order;

public record OrderDto(
        Long orderId,
        Long productId,
        String status
) {
    public static OrderDto from(Order order) {
        return new OrderDto(order.getId(), order.getProductId(), order.getStatus().toString());
    }
}
