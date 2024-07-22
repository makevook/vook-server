package vook.server.api.domain.user.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import vook.server.api.globalcommon.annotation.ModelFactory;

import java.util.ArrayList;
import java.util.UUID;

@ModelFactory
public class DefaultUserFactory implements UserFactory {

    @Override
    public User createForSignUpFromSocialOf(@NotEmpty @Email String email) {
        return User.builder()
                .uid(UUID.randomUUID().toString())
                .email(email)
                .status(UserStatus.SOCIAL_LOGIN_COMPLETED)
                .onboardingCompleted(false)
                .socialUsers(new ArrayList<>())
                .build();
    }
}
