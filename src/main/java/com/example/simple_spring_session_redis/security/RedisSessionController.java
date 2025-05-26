package com.example.simple_spring_session_redis.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisSessionController {

    private final RedisSessionService redisSessionService;

    @GetMapping("/sessions")
    Map<String, Map<Object, Object>> getSessions() {
        return redisSessionService.getAllSessions();
    }
}
