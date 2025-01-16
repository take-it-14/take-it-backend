package com.takeit.order.presentation.controller;

import java.util.UUID;

import com.takeit.order.application.dto.order.OrderCacheDto;
import com.takeit.order.application.dto.order.OrderCompleteDto;
import com.takeit.order.application.dto.product.ProductDto;
import com.takeit.order.application.service.OrderService;
import com.takeit.order.application.service.RedisService;
import com.takeit.order.domain.entity.Order;
import com.takeit.order.domain.repository.OrderRepository;
import com.takeit.order.infrastructure.client.ProductClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageListener {
    private final OrderService orderService;
    private final RedisService redisService;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @RabbitListener(queues = "${message.queues.order.cancel}")
    public void receiveMessage(Long orderId) {
        log.info("Received message");
        orderService.cancelOrder(orderId);
    }

    @Transactional
    @RabbitListener(queues = "${message.queues.order.complete}")
    public void handleOrderCompleteMessage(OrderCompleteDto orderCompleteDto) {
        OrderCacheDto orderCacheDto = redisService.getOrder(orderCompleteDto.orderId().toString());

        ProductDto productDto = productClient.getProductByUuid(orderCacheDto.productId());

        Order order = Order.create(
            orderCacheDto.uuid(),
            orderCacheDto.customerId(),
            productDto.id(),
            orderCacheDto.userCouponId(),
            orderCacheDto.quantity(),
            orderCacheDto.amount()
        );

        orderRepository.save(order);

        redisService.deleteOrder(order.getUuid().toString());
        redisService.deleteOrderId(order.getUuid().toString());
        // 재고 차감 db 반영 요청
    }
}
