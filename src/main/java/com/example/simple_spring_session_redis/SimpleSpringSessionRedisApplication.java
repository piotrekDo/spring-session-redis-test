package com.example.simple_spring_session_redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
public class SimpleSpringSessionRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSpringSessionRedisApplication.class, args);
    }

    @Bean
    public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        return CookieCsrfTokenRepository.withHttpOnlyFalse();
    }

}
