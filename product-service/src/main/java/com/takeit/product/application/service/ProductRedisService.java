package com.takeit.product.application.service;

import com.takeit.product.application.dto.product.CancelProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void cancelProduct(CancelProduct request) {
        String key = "product:" + request.productId();
        log.info("[cancel product] id : {}, amount : {}", request.productId(), request.quantity());
        redisTemplate.opsForValue().increment(key, request.quantity());
    }
}
