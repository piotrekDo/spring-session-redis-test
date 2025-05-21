package com.example.simple_spring_session_redis.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class GooglePrincipal {
    private final String firstName;
    private final String lastName;
    private final String pictureUrl;
    private final String email;
    private final String locale;
    private final String hd;
}
