package com.example.simple_spring_session_redis.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        String sessionId = sha.getSessionId();
        String name = sha.getUser().getName();
        logger.info("User " + name +  " subscribed to: {}", destination);

        // możesz teraz wysłać systemową wiadomość np. na czat
        if ("/topic/public".equals(destination)) {
            // np. przez SimpMessagingTemplate
        }
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        String sessionId = sha.getSessionId();
        String name = sha.getUser().getName();

        logger.info("Client unsubscribed. Session: " + name + ", Subscription: " + destination);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String name = sha.getUser().getName();
        String destination = sha.getDestination();
        String sessionId = event.getSessionId();
        logger.info("Session disconnected: {}", name);

    }
}
