package com.example.simple_spring_session_redis.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CsrfCookieFilter csrfCookieFilter;
    private final LoginSuccessHandler loginSuccessHandler;
    private final CustomCsrfDebugFilter customCsrfDebugFilter;

    private final static String ROLE_ADMIN = "ROLE_ADMIN";
    private final static String ROLE_USER = "USER";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(c -> {
                    CorsConfigurationSource cs = request -> {
                        CorsConfiguration cc = new CorsConfiguration();
                        cc.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
                        cc.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                        cc.setAllowedHeaders(List.of("Origin", "Content-Type", "X-Auth-Token", "Access-Control-Expose-Header"));
                        return cc;
                    };
                    c.configurationSource(cs);
                })
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(new CookieCsrfTokenRepository()))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())) // wyłącza XOR
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(csrfCookieFilter, SecurityContextHolderFilter.class)
//                .addFilterBefore(customCsrfDebugFilter, CsrfFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
                        .successHandler(loginSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("https://www.google.com")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "SESSION"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/bye").permitAll()
                        .requestMatchers("/role/admin").hasAuthority(ROLE_ADMIN)
                        .requestMatchers("/role/user").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)
                        .anyRequest().authenticated()
                )

        ;

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            String email = oAuth2User.getAttribute("email");

            List<GrantedAuthority> authorities = new ArrayList<>();
            if ("88stalker88@gmail.com".equalsIgnoreCase(email)) {
                authorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
                authorities.add(new SimpleGrantedAuthority(ROLE_USER));
            } else {
                authorities.add(new SimpleGrantedAuthority(ROLE_USER));
            }

            return new DefaultOAuth2User(
                    authorities,
                    oAuth2User.getAttributes(),
                    "email"
            );
        };
    }

}
