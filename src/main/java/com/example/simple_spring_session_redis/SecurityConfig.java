package com.example.simple_spring_session_redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public SecurityConfig(CsrfCookieFilter csrfCookieFilter, MyLoginSuccessHandler loginSuccessHandler) {
        this.csrfCookieFilter = csrfCookieFilter;
        this.loginSuccessHandler = loginSuccessHandler;
    }

    private final CsrfCookieFilter csrfCookieFilter;
    private final MyLoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .formLogin(login -> login
                        .defaultSuccessUrl("/", true)
                        .successHandler(loginSuccessHandler)
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
                ).addFilterBefore(csrfCookieFilter, SecurityContextHolderFilter.class)
        ;

        return http.build();
    }
}
