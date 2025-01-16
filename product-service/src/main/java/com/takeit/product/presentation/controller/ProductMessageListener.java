package com.takeit.product.presentation.controller;

import com.takeit.common.application.dto.CancelProduct;
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
	public void receiveProductCancelMessage(CancelProduct cancelProduct) {
		log.info("Received message: product cancel");

		productRedisService.cancelProduct(cancelProduct);
	}

	@RabbitListener(queues = "${message.queues.product.save.db}")
	public void receiveProductSaveDbMessage(UUID productId) {
		log.info("Received message: product save db");
		Product product = productRedisService.getProduct(productId);
		productRepository.save(product);
	}
}
