package vook.server.api.domain.user.model.social_user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import vook.server.api.domain.user.model.user.User;

public interface SocialUserFactory {

    SocialUser createForNewOf(
            @NotEmpty String provider,
            @NotEmpty String providerUserId,
            @NotNull User user
    );
}
