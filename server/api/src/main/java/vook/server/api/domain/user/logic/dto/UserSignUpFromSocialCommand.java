package vook.server.api.domain.user.logic.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import vook.server.api.domain.user.model.SocialUser;
import vook.server.api.domain.user.model.SocialUserFactory;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.model.UserFactory;

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
