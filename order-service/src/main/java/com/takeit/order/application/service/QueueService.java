package com.takeit.order.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.order.application.dto.queue.QueueDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.takeit.common.exception.ErrorCode.QUEUE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {
    @Autowired
    private final RedisTemplate<String ,String> stringRedisTemplate;

    private static final String WAITING_TOKENS = "waitingTokens";
    private static final String ACTIVE_TOKENS = "activeTokens";
    private static final String ACTIVE_USERS = "activeUsers";
    private static final int MAX_ACTIVE_SIZE = 100; // 최대 active token 수
    private static final long ACTIVE_TTL_SECONDS = 5; // active token TTL (5초)

    public boolean joinQueue(UUID productId, String username) {
        long currentTime = System.currentTimeMillis();
        String key = WAITING_TOKENS + ":productId:" + productId;
        stringRedisTemplate.opsForZSet().add(key, username, currentTime);
        log.info("username : {}, product id : {}", username, productId);

        return false;
    }

    public QueueDto getRankAndIsActive(UUID productId, String username) {
        String key = WAITING_TOKENS + ":productId:" + productId;

        Long rank = stringRedisTemplate.opsForZSet().rank(key, username);

        if(rank != null) {
            return QueueDto.of(rank, false);
        }

        String activeKey = ACTIVE_USERS + ":" + productId;

        Boolean isActive = stringRedisTemplate.opsForSet().isMember(activeKey, username);

        if(isActive != null)
            return QueueDto.of(0L, isActive);

        throw new CustomException(QUEUE_NOT_FOUND);
    }

    // 주문이 처리된 경우 활성화된 키를 삭제하는 메소드
    public void deleteActiveKey(UUID productId, String username) {
        String activeKey = "activeTokens:productId:" + productId.toString() + ":username:" + username;

        // TTL에 의한 자동 삭제 이전에 주문이 처리되면 해당 키를 즉시 삭제
        stringRedisTemplate.delete(activeKey);

        // 활성 사용자 목록에서 해당 사용자 삭제
        String activeSetKey = ACTIVE_USERS + ":productId:" + productId.toString();
        stringRedisTemplate.opsForSet().remove(activeSetKey, username);
    }

    private long getActiveUsersSize(UUID productId) {
        String activeSetKey = ACTIVE_USERS + ":productId:" + productId;
        Set<String> activeUsers = stringRedisTemplate.opsForSet().members(activeSetKey);

        return activeUsers != null ? activeUsers.size() : 0;
    }

    private void addActiveUser(UUID productId, String username) {
        String activeSetKey = ACTIVE_USERS + ":productId:" + productId.toString();
        String activeKey = ACTIVE_TOKENS + ":productId:" + productId.toString() + ":username:" + username;
        stringRedisTemplate.opsForSet().add(activeSetKey, username);
        stringRedisTemplate.opsForValue().set(activeKey, "active", ACTIVE_TTL_SECONDS, TimeUnit.SECONDS);
    }

    private Boolean hasWaitingToken(UUID productId) {
        String key = WAITING_TOKENS + ":productId:" + productId;
        return stringRedisTemplate.hasKey(key);
    }

    private Boolean hasActiveToken(UUID productId) {
        String key = ACTIVE_USERS + ":productId:" + productId;
        return stringRedisTemplate.hasKey(key);
    }

}
