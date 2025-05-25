package com.example.simple_spring_session_redis.security;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final GoogleOauth2LoginService loginService;

    @Value("${client.baseUrl}")
    private String clientBaseUrl;
    private String FAILURE_REDIRECTION = clientBaseUrl + "/login-failure";
    private String AUTHENTICATED_REDIRECTION = clientBaseUrl + "/oauth2/redirect";

    @PostConstruct
    void init() {
        FAILURE_REDIRECTION = clientBaseUrl + "/login-failure";
        AUTHENTICATED_REDIRECTION = clientBaseUrl + "/oauth2/redirect";
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Optional<GooglePrincipal> principalData = loginService.extractGooglePrincipalData(authentication);
        if (principalData.isEmpty()) {
            getRedirectStrategy().sendRedirect(request, response, FAILURE_REDIRECTION);
            log.warn("Unsuccessful google principal extraction");
            return;
        }

        GooglePrincipal googlePrincipal = principalData.get();
        HttpSession session = request.getSession();
        session.setAttribute("user", googlePrincipal.getEmail());
        loginService.appendUserCookie(response, googlePrincipal);
        getRedirectStrategy().sendRedirect(request, response, AUTHENTICATED_REDIRECTION);
    }
}
