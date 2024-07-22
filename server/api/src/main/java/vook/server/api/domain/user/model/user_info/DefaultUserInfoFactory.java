package vook.server.api.domain.user.model.user_info;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.globalcommon.annotation.ModelFactory;

@ModelFactory
public class DefaultUserInfoFactory implements UserInfoFactory {

    @Override
    public UserInfo createForRegisterOf(
            @NotEmpty String nickname,
            @NotNull Boolean marketingEmailOptIn,
            @NotNull User user
    ) {
        user.validateRegisterProcessReady();

        UserInfo userInfo = UserInfo.builder()
                .nickname(nickname)
                .marketingEmailOptIn(marketingEmailOptIn)
                .user(user)
                .build();
        user.register(userInfo);

        return userInfo;
    }
}
