package com.takeit.product.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.product.CancelProduct;
import com.takeit.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void saveProduct(Product product) {
        String key = "product:" + product.getUuid();
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("uuid", product.getUuid());
        productMap.put("sellerId", product.getSellerId());
        productMap.put("categoryId", product.getCategoryId());
        productMap.put("productName", product.getProductName());
        productMap.put("description", product.getDescription());
        productMap.put("price", product.getPrice());
        productMap.put("stock", product.getStock());
        productMap.put("limitPerUser", product.getLimitPerUser());
        productMap.put("openTime", product.getOpenTime().toString());
        productMap.put("closeTime", product.getCloseTime() != null ? product.getCloseTime().toString() : null);
        productMap.put("isActive", product.getIsActive());
        productMap.put("stars", product.getStars());

        redisTemplate.opsForHash().putAll(key, productMap);

        if (product.getCloseTime() != null) {
            long expireSeconds = Duration.between(LocalDateTime.now(), product.getCloseTime()).getSeconds();

            if (expireSeconds > 0) {
                redisTemplate.expire(key, Duration.ofSeconds(expireSeconds));
            }
        }

    }


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
