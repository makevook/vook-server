package vook.server.api.web.common.auth.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vook.server.api.globalcommon.helper.jwt.JWTReader;
import vook.server.api.web.common.auth.data.GeneratedToken;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${service.oauth2.accessTokenExpiredMinute}")
    private Integer accessTokenExpiredMinute;

    @Value("${service.oauth2.refreshTokenExpiredMinute}")
    private Integer refreshTokenExpiredMinute;

    private final JWTHelperProvider jwtHelperProvider;

    public GeneratedToken generateToken(String uid) {
        String access = buildAccessToken(uid);
        String refresh = buildRefreshToken(uid);
        return GeneratedToken.of(access, refresh);
    }

    public String validateAndGetUid(String token) {
        JWTReader reader = jwtHelperProvider.reader(token);
        reader.validate();
        return reader.getClaim("userUid");
    }

    public GeneratedToken refreshToken(String refreshToken) {
        JWTReader jwtReader = jwtHelperProvider.reader(refreshToken);
        jwtReader.validate();

        if (!"refresh".equals(jwtReader.getClaim("category"))) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String uid = jwtReader.getClaim("userUid");

        String access = buildAccessToken(uid);
        String refresh = buildRefreshToken(uid);

        return GeneratedToken.of(access, refresh);
    }

    private String buildAccessToken(String uid) {
        return jwtHelperProvider.writer()
                .withExpiredMs(1000L * 60 * accessTokenExpiredMinute)
                .withClaim("category", "access")
                .withClaim("userUid", uid)
                .jwtString();
    }

    private String buildRefreshToken(String uid) {
        return jwtHelperProvider.writer()
                .withExpiredMs(1000L * 60 * refreshTokenExpiredMinute)
                .withClaim("category", "refresh")
                .withClaim("userUid", uid)
                .jwtString();
    }
}
