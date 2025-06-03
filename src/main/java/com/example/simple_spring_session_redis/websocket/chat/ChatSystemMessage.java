package com.example.simple_spring_session_redis.websocket.chat;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ChatSystemMessage {
    private final Instant datetime;
    private final String name;
    private final String displayName;
    private final String imageUrl;
    private final MessageType type;

    public ChatSystemMessage(String name, String displayName, String imageUrl, MessageType type) {
        this.datetime = Instant.now();
        this.name = name;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public ChatSystemMessage(Instant datetime, String name, String displayName, String imageUrl, MessageType type) {
        this.datetime = datetime;
        this.name = name;
        this.displayName = displayName;
        this.imageUrl = imageUrl;
        this.type = type;
    }
}
