package com.takeit.product.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeit.product.application.dto.product.CancelProduct;
import com.takeit.product.application.service.ProductRedisService;
import com.takeit.product.domain.entity.Product;
import com.takeit.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductMessageListener {
    private final ProductRedisService productRedisService;
    private final ProductRepository productRepository;

    @RabbitListener(queues = "${message.queues.product.cancel}")
    public void receiveProductCancelMessage(String message) {
        log.info("Received message: product cancel");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CancelProduct cancelProduct = objectMapper.readValue(message, CancelProduct.class);
            productRedisService.cancelProduct(cancelProduct);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = "${message.queues.product.save.db}")
    public void receiveProductSaveDbMessage(String message) {
        log.info("Received message: product save db");
        try {
            Product product = productRedisService.getProduct(UUID.fromString(message));
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
