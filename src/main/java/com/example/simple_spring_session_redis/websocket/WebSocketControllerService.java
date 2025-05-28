package com.example.simple_spring_session_redis.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebSocketControllerService {
    private final SimpUserRegistry simpUserRegistry;

    public List<SimpUserDto> getActiveUsers() {
        return simpUserRegistry.getUsers().stream().map(SimpUserDto::fromSimpUser).collect(Collectors.toList());
    }
}