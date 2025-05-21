package com.example.simple_spring_session_redis;

import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener {

    @EventListener
    public void handleSessionCreated(SessionCreatedEvent event) {
        System.out.println("Session created: " + event.getSessionId());
    }

    @EventListener
    public void handleSessionDestroyed(SessionDeletedEvent event) {
        System.out.println("Session deleted: " + event.getSessionId());
    }

    @EventListener
    public void handleSessionExpired(SessionExpiredEvent event) {
        System.out.println("Session expired: " + event.getSessionId());
    }
}
