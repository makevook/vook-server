package vook.server.api.domain.user.logic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import vook.server.api.domain.user.model.SocialUser;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.model.UserFactory;

@Builder
public record UserSignUpFromSocialCommand(
        @NotBlank
        String provider,

        @NotBlank
        String providerUserId,

        @Email
        @NotBlank
        String email
) {
    public SocialUser toSocialUser(User user) {
        return SocialUser.forNewOf(provider, providerUserId, user);
    }

    public User toNewUser(UserFactory userFactory) {
        return userFactory.createForSignUpFromSocialOf(email);
    }
}
