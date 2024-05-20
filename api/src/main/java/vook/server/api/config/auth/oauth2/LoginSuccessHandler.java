package vook.server.api.config.auth.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import vook.server.api.app.auth.GeneratedToken;
import vook.server.api.app.auth.TokenService;
import vook.server.api.config.auth.common.VookLoginUser;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${service.oauth2.tokenNoticeUrl}")
    private String tokenNoticeUrl;

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        VookLoginUser oAuth2User = (VookLoginUser) authentication.getPrincipal();
        GeneratedToken token = tokenService.generateToken(oAuth2User.getUid());
        response.sendRedirect(buildRedirectUrl(token));
    }

    private String buildRedirectUrl(GeneratedToken token) {
        return UriComponentsBuilder.fromUriString(tokenNoticeUrl)
                .queryParam("access", token.getAccessToken())
                .queryParam("refresh", token.getRefreshToken())
                .build()
                .toUriString();
    }
}
