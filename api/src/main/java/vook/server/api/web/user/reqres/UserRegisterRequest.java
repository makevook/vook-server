package vook.server.api.web.user.reqres;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vook.server.api.domain.user.service.data.RegisterCommand;

public record UserRegisterRequest(
        @NotBlank
        @Size(min = 1, max = 10)
        String nickname,

        @NotNull
        @AssertTrue
        @Schema(allowableValues = {"true"})
        Boolean requiredTermsAgree,

        @NotNull
        Boolean marketingEmailOptIn
) {
    public RegisterCommand toCommand(String userUid) {
        return RegisterCommand.builder()
                .userUid(userUid)
                .nickname(nickname)
                .marketingEmailOptIn(marketingEmailOptIn)
                .build();
    }
}
