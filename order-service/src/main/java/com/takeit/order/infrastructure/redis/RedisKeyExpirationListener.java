package com.takeit.order.infrastructure.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.takeit.order.application.dto.order.OrderCacheDto;
import com.takeit.order.application.service.RedisService;
import com.takeit.order.presentation.controller.OrderMessageProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisKeyExpirationListener implements MessageListener {
	private final RedisService redisService;
	private final OrderMessageProducer orderMessageProducer;

	public RedisKeyExpirationListener(RedisService redisService, OrderMessageProducer orderMessageProducer) {
		this.redisService = redisService;
		this.orderMessageProducer = orderMessageProducer;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = message.toString();

		if(expiredKey.startsWith("orderId:")) handleOrderExpiration(expiredKey.substring(8));
	}

	private void handleOrderExpiration(String key){
		OrderCacheDto orderCacheDto = redisService.getOrder(key);
		// 재고 복구
		orderMessageProducer.sendProductCancelRequest(orderCacheDto.productId(), orderCacheDto.quantity());
		
		// 쿠폰 사용 복구

		redisService.deleteOrder(key);
	}
}
