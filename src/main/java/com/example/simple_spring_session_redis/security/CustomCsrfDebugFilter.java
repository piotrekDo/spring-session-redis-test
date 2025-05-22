package com.example.simple_spring_session_redis.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomCsrfDebugFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("X-XSRF-TOKEN");

        if (token != null) {
            String actualToken = request.getHeader(token.getHeaderName());
            System.out.println("== CSRF Debug ==");
            System.out.println("Expected token: " + token.getToken());
            System.out.println("Actual token:   " + actualToken);
        } else {
            System.out.println("== CSRF Debug == No CSRF token present in request.");
        }

        filterChain.doFilter(request, response);
    }
}