package vook.server.api.domain.user.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public interface UserInfoFactory {

    UserInfo createForRegisterOf(
            @NotEmpty String nickname,
            @NotNull Boolean marketingEmailOptIn,
            @NotNull User user
    );
}
