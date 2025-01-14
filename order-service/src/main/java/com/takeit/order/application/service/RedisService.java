package com.takeit.order.application.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.takeit.order.application.dto.order.OrderCacheDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public void saveOrderId(UUID orderId, long ttl){
		String key = "orderId:" + orderId;
		redisTemplate.opsForValue().set(key, orderId, ttl, TimeUnit.SECONDS);
	}

	public void saveOrder(OrderCacheDto orderCacheDto) {
		String key = "order:" + orderCacheDto.uuid().toString();
		redisTemplate.opsForValue().set(key, orderCacheDto);
	}

	public Object getOrderId(String orderId){
		String key = "orderId:" + orderId;
		return redisTemplate.opsForValue().get(key);
	}

	public OrderCacheDto getOrder(String orderId) {
		String key = "order:" + orderId;
		return (OrderCacheDto) redisTemplate.opsForValue().get(key);
	}

	public void deleteOrderId(String orderId){
		String key = "orderId:" + orderId;
		redisTemplate.delete(key);
	}

	public void deleteOrder(String orderId) {
		String key = "order:" + orderId;
		redisTemplate.delete(key);
	}
}
