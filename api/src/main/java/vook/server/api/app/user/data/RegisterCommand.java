package vook.server.api.app.user.data;

import lombok.Getter;

@Getter
public class RegisterCommand {

    private String userUid;
    private String nickname;
    private boolean marketingEmailOptIn;

    public static RegisterCommand of(
            String userUid,
            String nickname,
            boolean marketingEmailOptIn
    ) {
        RegisterCommand command = new RegisterCommand();
        command.userUid = userUid;
        command.nickname = nickname;
        command.marketingEmailOptIn = marketingEmailOptIn;
        return command;
    }
}
