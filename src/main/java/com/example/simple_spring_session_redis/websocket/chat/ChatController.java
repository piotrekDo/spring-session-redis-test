package com.example.simple_spring_session_redis.websocket.chat;

import com.example.simple_spring_session_redis.security.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message,
                            Principal principal,
                            Message<?> springMessage) {
        String destination = message.getDestination();
        CustomPrincipal customPrincipal = (CustomPrincipal) principal;
        message.setMessageId(12);
        message.setImageUrl(customPrincipal.getImageUrl());

        messagingTemplate.convertAndSend(destination, message);
    }

    @MessageMapping("/chat.private")
    public void sendPrivateMessage(ChatMessage message, Principal principal) {
        messagingTemplate.convertAndSendToUser(
                message.getDestination(), // nazwa użytkownika
                "/queue/private",       // zostanie zmapowane na /user/{recipient}/queue/private
                message
        );
    }
}
