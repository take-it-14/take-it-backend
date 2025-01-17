package com.takeit.order.infrastructure.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.takeit.order.application.dto.order.OrderCacheDto;
import com.takeit.order.application.service.OrderService;
import com.takeit.order.application.service.RedisService;
import com.takeit.order.presentation.controller.OrderMessageProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisKeyExpirationListener implements MessageListener {
	private final OrderService orderService;
	private final RedisService redisService;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = message.toString();

		if(expiredKey.startsWith("orderId:")) handleOrderExpiration(expiredKey.substring(8));
//		if(expiredKey.startsWith("activeTokens:")) handleActiveUserExpiration(expiredKey);
	}

	private void handleOrderExpiration(String key){
		log.info("RedisKeyExpirationListener - handleOrderExpiration");
		orderService.failOrder(key);
	}

	private void handleActiveUserExpiration(String key){
		String[] parts = key.split(":");
		String productId = parts[2];
		String username = parts[4];
		redisService.deleteActiveUser(productId, username);
	}
}
