package com.takeit.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReviewRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveReviewData(Long productId, int reviewCount, int totalStars) {
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("reviewCount", reviewCount);
        reviewData.put("totalStars", totalStars);

        redisTemplate.opsForHash().put("review:" + productId, "data", reviewData);
    }

    public Map<String, Object> getReviewData(Long productId) {
        return (Map<String, Object>) redisTemplate.opsForHash().get("review:" + productId, "data");
    }

    public void deleteReviewData(Long productId) {
        redisTemplate.delete("review:" + productId);
    }
}
