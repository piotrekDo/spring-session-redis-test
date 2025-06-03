package com.example.simple_spring_session_redis.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();

        switch (command) {

            case STOMP -> {
            }
            case CONNECT -> {
            }
            case DISCONNECT -> {
            }
            case SUBSCRIBE -> {
            }
            case UNSUBSCRIBE -> {
            }
            case SEND -> {
            }
            case ACK -> {
            }
            case NACK -> {
            }
            case BEGIN -> {
            }
            case COMMIT -> {
            }
            case ABORT -> {
            }
            case CONNECTED -> {
            }
            case RECEIPT -> {
            }
            case MESSAGE -> {
            }
            case ERROR -> {
            }
        }
        return message;
    }
}
