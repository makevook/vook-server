package vook.server.api.domain.user.service.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import vook.server.api.domain.user.model.SocialUser;
import vook.server.api.domain.user.model.User;

@Getter
public class SignUpFromSocialCommand {

    @NotBlank
    private String provider;

    @NotBlank
    private String providerUserId;

    @Email
    @NotBlank
    private String email;

    public static SignUpFromSocialCommand of(
            String provider,
            String providerUserId,
            String email
    ) {
        SignUpFromSocialCommand command = new SignUpFromSocialCommand();
        command.provider = provider;
        command.providerUserId = providerUserId;
        command.email = email;
        return command;
    }

    public SocialUser toSocialUser(User user) {
        return SocialUser.forNewOf(provider, providerUserId, user);
    }

    public User toNewUser() {
        return User.forSignUpFromSocialOf(email);
    }
}
