package vook.server.api.domain.user.logic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterCommand(
        @NotBlank
        String userUid,

        @NotBlank
        @Size(min = 1, max = 10)
        String nickname,

        @NotNull
        Boolean marketingEmailOptIn
) {
}
