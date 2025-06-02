package com.example.simple_spring_session_redis.security;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final GoogleOauth2LoginService loginService;
    private final CsrfTokenRepository csrfTokenRepository;


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
        CsrfToken token = csrfTokenRepository.loadToken(request);
        if (token == null) {
            token = csrfTokenRepository.generateToken(request);
        }
        csrfTokenRepository.saveToken(token, request, response);

        HttpSession session = request.getSession();
        GooglePrincipal googlePrincipal = principalData.get();
        session.setAttribute("user", googlePrincipal.getEmail());
        CustomPrincipal customPrincipal = new CustomPrincipal(
                googlePrincipal.getEmail(),
                googlePrincipal.getFirstName() + " " + googlePrincipal.getLastName(),
                session.getId(),
                Set.of("USER")
        );
        Authentication customAuth = new UsernamePasswordAuthenticationToken(
                customPrincipal,
                authentication.getCredentials(),
                authentication.getAuthorities()
        );

        loginService.appendUserCookie(response, googlePrincipal);
        SecurityContextHolder.getContext().setAuthentication(customAuth);
        getRedirectStrategy().sendRedirect(request, response, AUTHENTICATED_REDIRECTION);
    }
}
