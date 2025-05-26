package com.example.simple_spring_session_redis.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisSessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, Map<Object, Object>> getAllSessions() {
        Set<String> keys = redisTemplate.keys("spring:session:sessions:*");
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Map<Object, Object>> sessions = new HashMap<>();

        for (String key : keys) {
            // Dla każdego klucza pobierz całą hashę (czyli dane sesji)
            Map<Object, Object> sessionData = redisTemplate.opsForHash().entries(key);
            sessions.put(key, sessionData);
        }

        return sessions;
    }
}
