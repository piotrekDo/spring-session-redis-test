package com.example.simple_spring_session_redis.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class SimpUserDto {
    private final String name;
    private final Principal principal;
    private final boolean hasSessions;
    private List<SimpSessionDto> sessions;

    static SimpUserDto fromSimpUser(SimpUser simpUser) {
        List<SimpSessionDto> simpSessionDtos = simpUser.getSessions().stream()
                .map(SimpSessionDto::fromSimpSession)
                .toList();
        return new SimpUserDto(
                simpUser.getName(),
                simpUser.getPrincipal(),
                simpUser.hasSessions(),
                simpSessionDtos);
    }

    @AllArgsConstructor
    @Getter
    static class SimpSessionDto {
        private final String id;
        private final String user;
        private final List<SimpSubscriptionDto> subscriptions;

        static SimpSessionDto fromSimpSession(SimpSession simpSession) {
            return new SimpSessionDto(
                    simpSession.getId(),
                    simpSession.getUser().getName(),
                    simpSession.getSubscriptions().stream().map(SimpSubscriptionDto::fromSimpSubscription
                    ).collect(Collectors.toList())
            );
        }
    }

    @AllArgsConstructor
    @Getter
    static class SimpSubscriptionDto {
        private final String id;
        private final String sessionId;
        private final String destination;

        static SimpSubscriptionDto fromSimpSubscription(SimpSubscription simpSubscription) {
            return new SimpSubscriptionDto(
                    simpSubscription.getId(),
                    simpSubscription.getSession().getId(),
                    simpSubscription.getDestination()
            );
        }
    }
}
