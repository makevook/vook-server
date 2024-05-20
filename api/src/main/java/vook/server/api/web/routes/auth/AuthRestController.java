package vook.server.api.web.routes.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.app.auth.GeneratedToken;
import vook.server.api.app.auth.TokenService;
import vook.server.api.model.values.AuthValues;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController implements AuthApi {

    private final TokenService tokenService;

    @Override
    @GetMapping("/refresh")
    public ResponseEntity<Void> refreshToken(
            @RequestHeader(AuthValues.REFRESH_AUTHORIZATION_HEADER) String refresh,
            HttpServletResponse response
    ) {
        GeneratedToken token = tokenService.refreshToken(refresh);
        response.setHeader(AuthValues.AUTHORIZATION_HEADER, token.getAccessToken());
        response.setHeader(AuthValues.REFRESH_AUTHORIZATION_HEADER, token.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
