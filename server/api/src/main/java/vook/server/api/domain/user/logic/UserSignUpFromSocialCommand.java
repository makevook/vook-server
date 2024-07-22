package vook.server.api.domain.user.logic;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import vook.server.api.domain.user.model.social_user.SocialUser;
import vook.server.api.domain.user.model.social_user.SocialUserFactory;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.domain.user.model.user.UserFactory;

@Builder
public record UserSignUpFromSocialCommand(
        String provider,
        String providerUserId,

        @Email
        String email
) {
    public User toNewUser(UserFactory factory) {
        return factory.createForSignUpFromSocialOf(email);
    }

    public SocialUser toSocialUser(SocialUserFactory factory, User user) {
        return factory.createForNewOf(provider, providerUserId, user);
    }
}
