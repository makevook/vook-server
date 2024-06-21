package vook.server.api.app.context.user.data;

import lombok.Getter;

@Getter
public class RegisterCommand {

    private String userUid;
    private String nickname;
    private Boolean marketingEmailOptIn;

    public static RegisterCommand of(
            String userUid,
            String nickname,
            Boolean marketingEmailOptIn
    ) {
        RegisterCommand command = new RegisterCommand();
        command.userUid = userUid;
        command.nickname = nickname;
        command.marketingEmailOptIn = marketingEmailOptIn;
        return command;
    }
}
