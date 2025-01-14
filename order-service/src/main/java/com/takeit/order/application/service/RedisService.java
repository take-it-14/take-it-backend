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
		String key = "order:" + orderCacheDto.uuid();
		redisTemplate.opsForValue().set(key, orderCacheDto);
	}

	public Object getOrderId(UUID orderId){
		String key = "orderId:" + orderId;
		return redisTemplate.opsForValue().get(key);
	}

	public Object getOrder(UUID uuid) {
		String key = "order:" + uuid;
		return redisTemplate.opsForValue().get(key);
	}

	public void deleteOrderId(UUID orderId){
		String key = "orderId:" + orderId;
		redisTemplate.delete(key);
	}

	public void deleteOrder(UUID uuid) {
		String key = "order:" + uuid;
		redisTemplate.delete(key);
	}
}
