package com.example.simple_spring_session_redis.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChatMessage {
  private String sender;
  private String content;
  private MessageType type;


  enum MessageType {
    CHAT, JOIN, LEAVE
  }
}
