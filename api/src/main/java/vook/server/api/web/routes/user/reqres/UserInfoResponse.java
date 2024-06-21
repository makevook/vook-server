package vook.server.api.web.routes.user.reqres;

import lombok.Getter;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.user.domain.UserInfo;
import vook.server.api.app.contexts.user.domain.UserStatus;

@Getter
public class UserInfoResponse {

    private String uid;
    private String email;
    private UserStatus status;
    private Boolean onboardingCompleted;
    private String nickname;

    public static UserInfoResponse from(User user) {
        UserInfoResponse result = new UserInfoResponse();
        result.uid = user.getUid();
        result.email = user.getEmail();
        result.status = user.getStatus();
        result.onboardingCompleted = user.getOnboardingCompleted();
        UserInfo userInfo = user.getUserInfo();
        if (userInfo != null) {
            result.nickname = userInfo.getNickname();
        }
        return result;
    }
}
