package com.example.simple_spring_session_redis.websocket;

import com.example.simple_spring_session_redis.security.CustomPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;

@AllArgsConstructor
@Getter
public class EventInfo {
    private final CustomPrincipal principal;
    private final String userName;
    private final String displayName;
    private final String destination;
    private final String sessionId;

    public static EventInfo getEventInfo(AbstractSubProtocolEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        CustomPrincipal user = (CustomPrincipal) sha.getUser();
        String destination = sha.getDestination();
        String name = user.getName();
        String displayName = user.getDisplayName();
        return new EventInfo(user, name, displayName, destination, sessionId);
    }
}
