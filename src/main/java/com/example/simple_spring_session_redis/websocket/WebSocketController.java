package com.example.simple_spring_session_redis.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/socket")
public class WebSocketController {

    private final WebsocketSessionManager sessionManager;


    @GetMapping("/active-by-sub")
    List<ChatUser> getActiveUsersBySubscription(@RequestParam String sub) {
        return sessionManager.getActiveUsersBySubSubscription(sub);
    }
}
