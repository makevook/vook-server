package vook.server.api.web.routes.user.reqres;

import lombok.Getter;
import vook.server.api.model.user.User;
import vook.server.api.model.user.UserInfo;
import vook.server.api.model.user.UserStatus;

@Getter
public class UserInfoResponse {

    private String uid;
    private String email;
    private UserStatus status;
    private String nickname;

    public static UserInfoResponse from(User user) {
        UserInfoResponse result = new UserInfoResponse();
        result.uid = user.getUid();
        result.email = user.getEmail();
        result.status = user.getStatus();
        UserInfo userInfo = user.getUserInfo();
        if (userInfo != null) {
            result.nickname = userInfo.getNickname();
        }
        return result;
    }
}
