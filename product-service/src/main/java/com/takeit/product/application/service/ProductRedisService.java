package com.takeit.product.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.product.CancelProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String OCCUPY_SCRIPT =
            "local productKey = KEYS[1] " +
            "local decrementQuantity = tonumber(ARGV[1]) " +
            "local currentQuantity = tonumber(redis.call('GET', productKey)) " +
            "if not currentQuantity or currentQuantity < decrementQuantity then " +
            "   return -1 " +  // 재고 부족
            "end " +
            "redis.call('DECRBY', productKey, decrementQuantity) " +
            "return currentQuantity - decrementQuantity";

    public void cancelProduct(CancelProduct request) {
        String key = "product:" + request.productId();
        log.info("[cancel product] id : {}, amount : {}", request.productId(), request.quantity());
        redisTemplate.opsForValue().increment(key, request.quantity());
    }

    public void occupyProduct(Long productId, int quantity) {
        String productKey = "product:" + productId;
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(OCCUPY_SCRIPT);
        redisScript.setResultType(Long.class);

        Long result = redisTemplate.execute(redisScript, List.of(productKey), quantity);

        if(result == null || result == -1) {
            throw new CustomException(ErrorCode.INVALID_STOCK);
        }

    }
}
