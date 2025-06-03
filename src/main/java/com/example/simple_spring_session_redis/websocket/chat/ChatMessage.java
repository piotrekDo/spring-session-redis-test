package com.example.simple_spring_session_redis.websocket.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ChatMessage {
    private long messageId;
    private String destination;
    private Instant datetime = Instant.now();
    private String senderName;
    private String senderDisplayName;
    private String content;
    private String imageUrl;

    private MessageType type;

    public ChatMessage() {
    }

}
