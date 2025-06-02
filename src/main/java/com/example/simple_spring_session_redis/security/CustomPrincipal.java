package com.example.simple_spring_session_redis.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

@AllArgsConstructor
@Getter
public class CustomPrincipal implements Principal, Serializable {
    private final String name;
    private final String displayName;
    private final String sessionId;
    private final Set<String> authorities;

}
