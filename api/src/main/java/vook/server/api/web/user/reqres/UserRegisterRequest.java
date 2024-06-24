package vook.server.api.web.user.reqres;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import vook.server.api.domain.user.service.data.RegisterCommand;

@Data
public class UserRegisterRequest {

    @NotBlank
    @Size(min = 1, max = 10)
    private String nickname;

    @NotNull
    @AssertTrue
    @Schema(allowableValues = {"true"})
    private Boolean requiredTermsAgree;

    @NotNull
    private Boolean marketingEmailOptIn;

    public RegisterCommand toCommand(String userUid) {
        return RegisterCommand.of(
                userUid,
                nickname,
                marketingEmailOptIn
        );
    }
}
