package com.takeit.order.presentation.controller;

import com.takeit.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMessageListener {
    private final OrderService orderService;

    @RabbitListener(queues = "${message.queues.order.cancel}")
    public void receiveMessage(Long orderId) {
        log.info("Received message");
        orderService.cancelOrder(orderId);
    }
}
