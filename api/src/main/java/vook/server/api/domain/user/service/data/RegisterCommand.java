package vook.server.api.domain.user.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterCommand {

    @NotBlank
    private String userUid;

    @NotBlank
    @Size(min = 1, max = 10)
    private String nickname;

    @NotNull
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
