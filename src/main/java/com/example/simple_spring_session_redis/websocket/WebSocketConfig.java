package com.example.simple_spring_session_redis.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/ws")
              .setHandshakeHandler(new DefaultHandshakeHandler() {
                @Override
                protected Principal determineUser(ServerHttpRequest request,
                                                  WebSocketHandler wsHandler,
                                                  Map<String, Object> attributes) {
                  return (Principal) attributes.get("principal");
                }
              })
              .addInterceptors(new CustomPrincipalHandshakeInterceptor())
              .setAllowedOrigins("*");
    }
}
