package vook.server.api.domain.user.model.user_info;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import vook.server.api.domain.user.model.user.User;

public interface UserInfoFactory {

    UserInfo createForRegisterOf(
            @NotEmpty String nickname,
            @NotNull Boolean marketingEmailOptIn,
            @NotNull User user
    );
}
