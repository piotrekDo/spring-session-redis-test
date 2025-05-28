package com.example.simple_spring_session_redis.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new AuthChannelInterceptor());
  }
}
