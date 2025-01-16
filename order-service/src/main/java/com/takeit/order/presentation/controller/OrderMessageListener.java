package com.takeit.order.presentation.controller;

import java.util.UUID;

import com.takeit.order.application.dto.order.OrderCacheDto;
import com.takeit.common.application.dto.OrderUuidDto;
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
    public void handleOrderFailMessage(OrderUuidDto orderUuidDto) {
        log.info("handleOrderFailMessage");

        orderService.failOrder(orderUuidDto.orderId().toString());

        redisService.deleteOrderId(orderUuidDto.orderId().toString());
    }

    @Transactional
    @RabbitListener(queues = "${message.queues.order.complete}")
    public void handleOrderCompleteMessage(OrderUuidDto orderUuidDto) {
        OrderCacheDto orderCacheDto = redisService.getOrder(orderUuidDto.orderId().toString());

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
