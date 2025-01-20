package com.takeit.auth.application.service;

import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveUser(User user) {
        String key = "username:" + user.getUsername();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("nickname", user.getNickname());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole());


        redisTemplate.opsForHash().putAll(key, userMap);
        redisTemplate.expire(key, Duration.ofDays(1));
    }

    public User getUser(String username) {
        String key = "username:" + username;

        Map<Object, Object> userMap = redisTemplate.opsForHash().entries(key);
        if (userMap.isEmpty()) {
            return null;
        }

        return User.of(
                Long.valueOf(userMap.get("id").toString()),
                username,
                userMap.get("nickname").toString(),
                userMap.get("email").toString(),
                UserRole.valueOf(userMap.get("role").toString())
        );
    }

    public void deleteUser(String username) {
        String key = "username:" + username;
        redisTemplate.delete(key);
    }


}
