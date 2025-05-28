package com.example.simple_spring_session_redis.security;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

public class CustomPrincipal implements Principal, Serializable {
    private final String name;
    private final String sessionId;
    private final Set<String> authorities;

    public CustomPrincipal(String name, String sessionId, Set<String> authorities) {
        this.name = name;
        this.sessionId = sessionId;
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}
