package com.example.simple_spring_session_redis.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/websocketInfo")
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketControllerService service;


    @GetMapping("/all")
    List<SimpUserDto> getAllUsers() {
        return service.getActiveUsers();
    }
}
