package vook.server.api.web.routes.user.reqres;

import lombok.Data;
import vook.server.api.app.user.data.RegisterCommand;

@Data
public class UserRegisterRequest {

    private String nickname;
    private boolean requiredTermsAgree;
    private boolean marketingEmailOptIn;

    public RegisterCommand toCommand(String userUid) {
        return RegisterCommand.of(
                userUid,
                nickname,
                marketingEmailOptIn
        );
    }
}
