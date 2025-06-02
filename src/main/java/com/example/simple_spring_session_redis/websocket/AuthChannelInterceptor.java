package com.example.simple_spring_session_redis.websocket;

import com.example.simple_spring_session_redis.security.CustomPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        StompCommand command = accessor.getCommand();
//        CustomPrincipal user = (CustomPrincipal) accessor.getUser();
//
//        if (StompCommand.CONNECT.equals(command)) {
//            System.out.println("CONNECT: " + accessor.getDestination());
//        } else if (StompCommand.SUBSCRIBE.equals(command)) {
//            System.out.println("SUBSCRIBE: " + accessor.getDestination());
//        } else if (StompCommand.MESSAGE.equals(command)) {
//            System.out.println("MESSAGE: " + accessor.getDestination());
//        } else if (StompCommand.UNSUBSCRIBE.equals(command)) {
//            System.out.println("UNSUBSCRIBE: " + accessor.getDestination());
//            System.out.println("UNSUBSCRIBE: " + accessor.getDestination());
//            System.out.println("UNSUBSCRIBE: " + accessor.getDestination());
//        } else if (StompCommand.DISCONNECT.equals(command)) {
//            System.out.println("UNSUBSCRIBE: " + accessor.getDestination());
//            System.out.println("UNSUBSCRIBE: " + accessor.getDestination());
//            System.out.println("UNSUBSCRIBE: " + accessor.getDestination());
//        }
        return message;
    }
}
