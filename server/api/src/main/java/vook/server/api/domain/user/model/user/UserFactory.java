package vook.server.api.domain.user.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public interface UserFactory {
    User createForSignUpFromSocialOf(@NotEmpty @Email String email);
}
