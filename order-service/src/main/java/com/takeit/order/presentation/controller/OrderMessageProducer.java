package com.takeit.order.presentation.controller;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.takeit.order.application.dto.product.CancelProduct;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderMessageProducer {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queues.product.cancel}")
	private String productQueue;

	@Value("${message.queues.coupon.cancel}")
	private String couponQueue;

	private void sendMessage(String routingKey, Object message) {
		rabbitTemplate.convertAndSend(routingKey, message);
	}

	public void sendProductCancelRequest(UUID productId, Long quantity){
		sendMessage(productQueue, CancelProduct.create(productId, quantity));
	}

	public void sendUserCouponRequest(Long userCouponId) {
		sendMessage(couponQueue, userCouponId);
	}
}
