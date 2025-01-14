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

	public void saveOrder(OrderCacheDto orderCacheDto, long ttl) {
		String key = "order:" + orderCacheDto.uuid();
		redisTemplate.opsForValue().set(key, orderCacheDto, ttl, TimeUnit.SECONDS);
	}

	public Object getOrder(UUID uuid) {
		String key = "order:" + uuid;
		return redisTemplate.opsForValue().get(key);
	}

	public void deleteOrder(UUID uuid) {
		String key = "order:" + uuid;
		redisTemplate.delete(key);
	}
}
