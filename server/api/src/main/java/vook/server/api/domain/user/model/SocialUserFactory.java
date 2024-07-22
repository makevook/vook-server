package vook.server.api.domain.user.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface SocialUserFactory {
    
    SocialUser createForNewOf(
            @NotEmpty String provider,
            @NotEmpty String providerUserId,
            @NotNull User user
    );
}
