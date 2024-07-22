package vook.server.api.web.user.reqres;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vook.server.api.domain.user.logic.UserRegisterCommand;

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
    public UserRegisterCommand toCommand(String userUid) {
        return UserRegisterCommand.builder()
                .userUid(userUid)
                .nickname(nickname)
                .marketingEmailOptIn(marketingEmailOptIn)
                .build();
    }
}
