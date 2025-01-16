package com.takeit.payment.presentation.controller;

import java.util.UUID;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.takeit.common.application.dto.OrderUuidDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMessageProducer {

	private final RabbitTemplate rabbitTemplate;
	private final TopicExchange paymentExchange;

	@Value("${message.queues.order.complete}")
	private String orderComplete;

	private void sendMessage(String routingKey, Object message) {
		rabbitTemplate.convertAndSend(paymentExchange.getName(), routingKey, message);
	}

	public void sendOrderCompleteRequest(UUID orderId){
		sendMessage(orderComplete, OrderUuidDto.from(orderId));
	}
}
