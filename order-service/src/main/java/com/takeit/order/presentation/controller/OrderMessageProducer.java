package com.takeit.order.presentation.controller;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.takeit.common.application.dto.CancelProduct;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderMessageProducer {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queues.product.cancel}")
	private String productQueue;

	@Value("${message.queues.coupon.cancel}")
	private String couponQueue;

	@Value("${message.queues.product.save.db}")
	private String saveProductDbQueue;

	private void sendMessage(String routingKey, Object message) {
		rabbitTemplate.convertAndSend(routingKey, message);
	}

	public void sendProductCancelRequest(UUID productId, Long quantity){
		sendMessage(productQueue, CancelProduct.create(productId, quantity));
	}

	public void sendUserCouponCancelRequest(Long userCouponId) {
		sendMessage(couponQueue, userCouponId);
	}

	public void sendProductSaveDbRequest(UUID productId){ sendMessage(saveProductDbQueue, productId); }
}
