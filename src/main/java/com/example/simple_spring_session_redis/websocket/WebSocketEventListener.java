package com.example.simple_spring_session_redis.websocket;

import com.example.simple_spring_session_redis.security.CustomPrincipal;
import com.example.simple_spring_session_redis.websocket.chat.ChatSystemMessage;
import com.example.simple_spring_session_redis.websocket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Set;

import static com.example.simple_spring_session_redis.websocket.EventInfo.getEventInfo;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final WebsocketSessionManager sessionManager;
    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        EventInfo eventInfo = getEventInfo(event);
        String destination = eventInfo.getDestination();
        String name = eventInfo.getUserName();
        CustomPrincipal principal = eventInfo.getPrincipal();
        String displayName = eventInfo.getDisplayName();
        logger.info("User " + name + " subscribed to: {}", destination);

        sessionManager.addUserToChannel(principal, destination);
        messagingTemplate.convertAndSend(destination, new ChatSystemMessage(name, displayName, principal.getImageUrl(), MessageType.JOIN));
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        EventInfo eventInfo = getEventInfo(event);
        CustomPrincipal principal = eventInfo.getPrincipal();
        String destination = eventInfo.getDestination();
        String name = eventInfo.getUserName();
        String displayName = eventInfo.getDisplayName();

        sessionManager.removeUserFromChannel(principal, destination);
        logger.info("Client unsubscribed. Session: " + name + ", Subscription: " + destination);
        messagingTemplate.convertAndSend(destination, new ChatSystemMessage(name, displayName, principal.getImageUrl(), MessageType.LEAVE));

    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        EventInfo eventInfo = getEventInfo(event);
        CustomPrincipal principal = eventInfo.getPrincipal();
        String name = eventInfo.getUserName();
        String displayName = eventInfo.getDisplayName();

        Set<String> userChannels = sessionManager.removeUserFromAllChannels(principal);
        userChannels.forEach(channelName -> messagingTemplate.convertAndSend(channelName, new ChatSystemMessage(name, displayName, principal.getImageUrl(), MessageType.OFFLINE)));
        logger.info("Session disconnected: {}", name);
    }
}
