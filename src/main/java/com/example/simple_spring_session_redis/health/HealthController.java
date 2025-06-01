package com.example.simple_spring_session_redis.health;

import com.example.simple_spring_session_redis.websocket.SimpUserDto;
import com.example.simple_spring_session_redis.websocket.WebSocketControllerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {
    private final WebSocketControllerService service;

    @GetMapping("/websocket/sessions")
    List<SimpUserDto> getAllUsers() {
        return service.getActiveUsers();
    }
}
