package vook.server.api.app.domain.user.data;

import lombok.Getter;
import vook.server.api.app.domain.user.model.SocialUser;
import vook.server.api.app.domain.user.model.User;

@Getter
public class SignUpFromSocialCommand {

    private String provider;
    private String providerUserId;
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
