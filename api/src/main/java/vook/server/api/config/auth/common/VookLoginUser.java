package vook.server.api.config.auth.common;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import vook.server.api.helper.jwt.JWTReader;
import vook.server.api.helper.jwt.JWTWriter;
import vook.server.api.model.user.SocialUser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@ToString
public class VookLoginUser implements OAuth2User {

    private String uid;

    public static VookLoginUser of(
            String uid
    ) {
        VookLoginUser user = new VookLoginUser();
        user.uid = uid;
        return user;
    }

    public static VookLoginUser from(SocialUser user) {
        return VookLoginUser.of(user.getUser().getUid());
    }

    public static VookLoginUser from(JWTReader jwtReader) {
        return VookLoginUser.of(jwtReader.getClaim("uid"));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return this.uid;
    }

    public String toAccessToken(JWTWriter jwtWriter, Integer accessTokenExpiredMinute) {
        return jwtWriter
                .withExpiredMs(1000L * 60 * accessTokenExpiredMinute)
                .withClaim("category", "access")
                .withClaim("uid", this.uid)
                .jwtString();
    }

    public String toRefreshToken(JWTWriter jwtWriter, Integer refreshTokenExpiredMinute) {
        return jwtWriter
                .withExpiredMs(1000L * 60 * refreshTokenExpiredMinute)
                .withClaim("category", "refresh")
                .withClaim("uid", this.uid)
                .jwtString();
    }
}
