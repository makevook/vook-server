package vook.server.api.domain.user.model.social_user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.globalcommon.annotation.ModelFactory;

@ModelFactory
public class DefaultSocialUserFactory implements SocialUserFactory {

    @Override
    public SocialUser createForNewOf(
            @NotEmpty String provider,
            @NotEmpty String providerUserId,
            @NotNull User user
    ) {
        SocialUser socialUser = SocialUser.builder()
                .provider(provider)
                .providerUserId(providerUserId)
                .user(user)
                .build();

        user.addSocialUser(socialUser);
        return socialUser;
    }
}
