package vook.server.api.web.auth.data;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

}
