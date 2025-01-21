package com.takeit.order.application.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveTokenScheduler {

    private static final String WAITING_TOKENS = "waitingTokens";
    private static final String ACTIVE_TOKENS = "activeTokens";
    private static final String ACTIVE_USERS = "activeUsers";
    private static final String WAITING_USERS = "waitingUsers";
    @Value("${queue.max-active-size}")
    private int MAX_ACTIVE_SIZE; // 최대 active token 수
    private static final long ACTIVE_TTL_SECONDS = 5; // active token TTL (5초)

    private final RedisTemplate<String, String> redisTemplate;

//    @Scheduled(fixedRate = 2000)
//    public void manageActiveTokens() {
//        Set<UUID> productIds = getAllProductIds();
//        for (UUID productId : productIds) {
//            processProductQueue(productId);
//        }
//        log.info("Scheduler end");
//    }

    @Scheduled(fixedRate = 2000)
    public void manageActiveTokensInOneQueue() {
        Set<String> keys = getAllActiveTokens();
        int activeUserSize = keys == null ? 0 : keys.size();

        if(activeUserSize >= MAX_ACTIVE_SIZE) {
            log.info("active token full... Scheduler end");
            return;
        }

        Set<String> keyForMove = redisTemplate.opsForZSet().range(WAITING_TOKENS, 0, MAX_ACTIVE_SIZE - activeUserSize - 1);

        if(keyForMove == null) {
            log.info("keyForMove is null");
            return;
        }

        log.info("current waiting token size : {}", redisTemplate.opsForZSet().size(WAITING_TOKENS));

        for (String key : keyForMove) {
            String activeKey = ACTIVE_TOKENS + ":username:" + key;
            redisTemplate.opsForValue().set(activeKey, "active", ACTIVE_TTL_SECONDS, TimeUnit.SECONDS);
            log.info("create active token : {}", activeKey);
            redisTemplate.opsForZSet().remove(WAITING_TOKENS, key);
            log.info("remove waiting token : {}", key);
            redisTemplate.delete(WAITING_USERS + ":username:" + key);
        }

        log.info("finish key move");
        log.info("current waiting token size : {}", redisTemplate.opsForZSet().size(WAITING_TOKENS));
    }


    @Async
    public void processProductQueue(UUID productId) {
        String activeUsersKey = ACTIVE_USERS + ":productId:" + productId;
        Set<String> activeUsers = redisTemplate.opsForSet().members(activeUsersKey);
        long activeSize = activeUsers != null ? (long)activeUsers.size() : 0;

        // 활성 큐가 최대 크기에 도달했는지 확인
        long tokensToAdd = MAX_ACTIVE_SIZE - activeSize;
        if (tokensToAdd <= 0) {
            return;
        }

        String waitingKey = WAITING_TOKENS + ":productId:" + productId;
        String activeSetKey = ACTIVE_USERS + ":productId:" + productId;

        Set<String> keysToMove = redisTemplate.opsForZSet().range(waitingKey, 0, tokensToAdd - 1);
        if (keysToMove == null || keysToMove.isEmpty()) {
            return;
        }

        for (String username : keysToMove) {
            String activeKey = ACTIVE_TOKENS + ":productId:" + productId + ":username:" + username;
            redisTemplate.opsForValue().set(activeKey, "active", ACTIVE_TTL_SECONDS, TimeUnit.SECONDS);
            redisTemplate.opsForSet().add(activeSetKey, username);
            redisTemplate.opsForZSet().remove(waitingKey, username);
        }
        log.info("Finished processing queue for productId: {}", productId);
    }

    public Set<UUID> getAllProductIds() {
        Set<UUID> productIds = new HashSet<>();
        String pattern = WAITING_TOKENS + ":*"; // 상품별 대기 큐 키 패턴

        // SCAN을 사용하여 효율적으로 키 스캔
        Cursor<byte[]> cursor = redisTemplate.execute((RedisCallback<Cursor<byte[]>>) connection -> {
            return connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build());
        });

        while (cursor.hasNext()) {
            byte[] keyBytes = cursor.next();
            // byte[]를 String으로 변환
            String key = new String(keyBytes, StandardCharsets.UTF_8);
            // 상품 ID 추출 (예: "waitingTokens:1" -> 1)
            productIds.add(UUID.fromString(key.split(":")[2]));
        }

        log.info("getAllProductIds size : {}", productIds.size());
        return productIds;
    }

    public Set<String> getAllActiveTokens() {
        Set<String> activeTokens = new HashSet<>();
        String pattern = ACTIVE_TOKENS + ":*"; // 상품별 대기 큐 키 패턴

        // SCAN을 사용하여 효율적으로 키 스캔
        Cursor<byte[]> cursor = redisTemplate.execute((RedisCallback<Cursor<byte[]>>) connection -> {
            return connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build());
        });

        while (cursor.hasNext()) {
            byte[] keyBytes = cursor.next();
            // byte[]를 String으로 변환
            String key = new String(keyBytes, StandardCharsets.UTF_8);
            // 상품 ID 추출 (예: "waitingTokens:1" -> 1)
            activeTokens.add(key);
        }

        log.info("get all active key : {}", activeTokens.size());
        return activeTokens;
    }


}
