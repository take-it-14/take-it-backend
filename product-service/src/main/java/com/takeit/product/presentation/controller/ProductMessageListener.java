package com.takeit.product.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeit.product.application.dto.product.CancelProduct;
import com.takeit.product.application.service.ProductRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMessageListener {
    private final ProductRedisService productRedisService;

    @RabbitListener(queues = "${message.queue.product.cancel}")
    public void receiveMessage(String message) {
        log.info("Received message");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CancelProduct cancelProduct = objectMapper.readValue(message, CancelProduct.class);
            productRedisService.cancelProduct(cancelProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
