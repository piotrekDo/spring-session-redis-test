package com.example.simple_spring_session_redis.websocket.chat;

import com.example.simple_spring_session_redis.security.CustomPrincipal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ChatUser {
    private final String name;
    private final String displayName;
    private final String sessionId;
    private final String imageUrl;

    public static ChatUser fromPrincipal(CustomPrincipal principal) {
        return new ChatUser(principal.getName(), principal.getDisplayName(), principal.getSessionId(), principal.getImageUrl());
    }
}
