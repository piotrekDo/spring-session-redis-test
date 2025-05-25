package com.example.simple_spring_session_redis.security;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@Slf4j
public class GoogleOauth2LoginService {
    private static final String USER_COOKIE_NAME = "user_info";

    void appendUserCookie(HttpServletResponse response, GooglePrincipal principal) {
        String userInfo = URLEncoder.encode(principal.getFirstName() + "|" + principal.getEmail(), StandardCharsets.UTF_8);

        Cookie userCookie = new Cookie(USER_COOKIE_NAME, userInfo);
        userCookie.setPath("/");
        userCookie.setHttpOnly(false);
        response.addCookie(userCookie);
    }

    Optional<GooglePrincipal> extractGooglePrincipalData(Authentication authentication) {
        final String firstName;
        final String lastName;
        final String pictureUrl;
        final String email;
        final String locale;
        final String hd;
        try {
            Object principal = authentication.getPrincipal();
            OAuth2User princpipalOAuth2 = (OAuth2User) principal;
            firstName = (String) princpipalOAuth2.getAttributes().get("given_name");
            lastName = (String) princpipalOAuth2.getAttributes().get("family_name");
            pictureUrl = (String) princpipalOAuth2.getAttributes().get("picture");
            email = (String) princpipalOAuth2.getAttributes().get("email");
            locale = (String) princpipalOAuth2.getAttributes().get("locale");
            hd = (String) princpipalOAuth2.getAttributes().get("hd");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
        return Optional.of(new GooglePrincipal(firstName, lastName, pictureUrl, email, locale, hd));
    }

    void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}

