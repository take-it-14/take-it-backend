package com.takeit.order.application.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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
public class ActiveTokenScheduler {

    private static final String WAITING_TOKENS = "waitingTokens";
    private static final String ACTIVE_TOKENS = "activeTokens";
    private static final String ACTIVE_USERS = "activeUsers";
    private static final int MAX_ACTIVE_SIZE = 100; // 최대 active token 수
    private static final long ACTIVE_TTL_SECONDS = 5; // active token TTL (5초)

    private final RedisTemplate<String, String> redisTemplate;

    public ActiveTokenScheduler(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    @Scheduled(fixedRate = 2000) // 1초마다 실행
//    public void manageActiveTokens() {
//        for (UUID productId : getAllProductIds()) { // 상품 ID 목록 가져오기
//            // 각 상품에 대한 대기 큐 키를 설정 (상품별 대기 큐)
//            log.info("scheduler : {}", productId);
//            String waitingKey = WAITING_TOKENS + ":productId:" + productId; // 각 상품별 대기 큐
//            String activeKeyPrefix = ACTIVE_TOKENS + ":productId:" + productId + ":username:";
//
//            // 활성 사용자 목록 Set에 저장
//
//            // 활성 큐 크기 확인
//            String activeUsersKey = ACTIVE_USERS + ":productId:" + productId;
//
//            Set<String> activeUsers = redisTemplate.opsForSet().members(activeUsersKey);
//            long activeSize = activeUsers != null ? (long)activeUsers.size() : 0;
//            log.info("activeSize : {}", activeSize);
//
//            // 활성 큐가 최대 크기에 도달했는지 확인
//            long tokensToAdd = MAX_ACTIVE_SIZE - activeSize;
//            if (tokensToAdd <= 0) {
//                continue; // 더 추가할 필요 없음
//            }
//
//            log.info("tokensToAdd : {}", tokensToAdd);
//
//            // 대기 큐에서 상위 tokensToAdd 개 가져오기
//            Set<String> keysToMove = redisTemplate.opsForZSet().range(waitingKey, 0, tokensToAdd - 1);
//            if (keysToMove == null || keysToMove.isEmpty()) {
//                log.info("keysToMove is empty");
//                continue; // 대기열에 더 이상 추가할 키가 없음
//            }
//
//            log.info("keysToMove : {}", keysToMove.size());
//
//            String activeSetKey = ACTIVE_USERS + ":productId:" + productId;
//
//            // 활성 사용자 목록에 사용자 추가
//            for (String username : keysToMove) {
//                username = username.replace("\"", "").trim();
//                String activeKey = activeKeyPrefix + username;
//
//                log.info("active key : {}", activeKey);
//                // 개별 키에 TTL을 설정하여 활성 상태로 표시
//                redisTemplate.opsForValue().set(activeKey, "active", ACTIVE_TTL_SECONDS, TimeUnit.SECONDS);
//                String value = redisTemplate.opsForValue().get(activeKey);
//                log.info("Stored value: {}", value);
//
//                // Set에 사용자 추가
//                redisTemplate.opsForSet().add(activeSetKey, username);
//
//                // 대기 큐에서 삭제
//                redisTemplate.opsForZSet().remove(waitingKey, username);
//
//            }
//            log.info("scheduler finish : {}", productId);
//        }
//        log.info("scheduler end");
    //    }
    @Async
    @Scheduled(fixedRate = 2000)
    public void manageActiveTokens() {
        for (UUID productId : getAllProductIds()) {
            processProductQueue(productId);
        }
        log.info("Scheduler end");
    }

    @Async
    public void processProductQueue(UUID productId) {
        String activeUsersKey = ACTIVE_USERS + ":productId:" + productId;
        Set<String> activeUsers = redisTemplate.opsForSet().members(activeUsersKey);
        long activeSize = activeUsers != null ? (long)activeUsers.size() : 0;
        log.info("activeSize : {}", activeSize);

        // 활성 큐가 최대 크기에 도달했는지 확인
        long tokensToAdd = MAX_ACTIVE_SIZE - activeSize;
        if (tokensToAdd <= 0) {
            return;
        }

        log.info("Processing queue for productId: {}", productId);
        String waitingKey = WAITING_TOKENS + ":productId:" + productId;
        String activeSetKey = ACTIVE_USERS + ":productId:" + productId;

        Set<String> keysToMove = redisTemplate.opsForZSet().range(waitingKey, 0, MAX_ACTIVE_SIZE - 1);
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

        log.info("getAllProductIds : {}", productIds.size());
        return productIds;
    }
}
