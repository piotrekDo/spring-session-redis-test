package com.example.simple_spring_session_redis.websocket;

import com.example.simple_spring_session_redis.security.CustomPrincipal;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.simple_spring_session_redis.websocket.ChatUser.fromPrincipal;

@Component
public class WebsocketSessionManager {

    private static final Map<String, Set<ChatUser>> channelToUsers = new ConcurrentHashMap<>();

    static {
        channelToUsers.put("/topic/public", Collections.synchronizedSet(new HashSet<>()));
    }

    void addUserToChannel(CustomPrincipal user, String channel) {
        channelToUsers.computeIfAbsent(channel, key -> ConcurrentHashMap.newKeySet()).add(fromPrincipal(user));
    }

    void removeUserFromChannel(CustomPrincipal user, String channel) {
        Set<ChatUser> usersSet = channelToUsers.get(channel);
        if (usersSet != null) {
            usersSet.remove(fromPrincipal(user));
        }
    }

    Set<String> removeUserFromAllChannels(CustomPrincipal user) {
        Set<String> removedFromChannels = new HashSet<>();

        channelToUsers.forEach((channel, usersSet) -> {
            if (usersSet.remove(fromPrincipal(user))) {
                removedFromChannels.add(channel);
            }
        });
        channelToUsers.entrySet().forEach(System.out::println);
        return removedFromChannels;
    }

    List<ChatUser> getActiveUsersBySubSubscription(String sub) {
        return new ArrayList<>(channelToUsers.get(sub));
    }
}
