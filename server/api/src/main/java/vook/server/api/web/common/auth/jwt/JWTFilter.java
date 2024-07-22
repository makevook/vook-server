package vook.server.api.web.common.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vook.server.api.web.common.auth.app.TokenService;
import vook.server.api.web.common.auth.data.AuthValues;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getAccessToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        OAuth2User oAuth2User;
        try {
            oAuth2User = VookLoginUser.of(tokenService.validateAndGetUid(token));
        } catch (Exception e) {
            log.error("JWT validation failed", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //스프링 시큐리티 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.authenticated(oAuth2User, null, oAuth2User.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String AuthorizationHeaderValue = request.getHeader(AuthValues.AUTHORIZATION_HEADER);
        if (AuthorizationHeaderValue == null) {
            return null;
        }

        String token = AuthorizationHeaderValue.replaceFirst("Bearer", "").trim();
        if (!StringUtils.hasText(token)) {
            return null;
        }

        return token;
    }
}
