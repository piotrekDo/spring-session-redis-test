package com.example.simple_spring_session_redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Wartości można zostawić domyślne (lub ustawić np. JSON serializer)
        // template.setValueSerializer(...);
        // template.setHashValueSerializer(...);

        template.afterPropertiesSet();
        return template;
    }

}
