package com.example.simple_spring_session_redis.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.session.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomPrincipalFilter extends OncePerRequestFilter {

    private final RedisIndexedSessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            String sessionId = httpSession.getId();
            Session session = sessionRepository.findById(sessionId);
            if (session != null) {
                SecurityContext securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
                if (securityContext != null) {
                    Authentication auth = securityContext.getAuthentication();
                    if (auth != null && auth.getPrincipal() instanceof CustomPrincipal customPrincipal) {
                        Authentication customAuth = new UsernamePasswordAuthenticationToken(
                                customPrincipal,
                                auth.getCredentials(),
                                auth.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(customAuth);
                    }
                }
            } else {
                System.err.println("CustomPrincipalFilter: SESSION IS NULL");
            }
        }
        filterChain.doFilter(request, response);
    }
}
