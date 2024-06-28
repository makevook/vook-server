package vook.server.api.domain.user.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterCommand(
        @NotBlank
        String userUid,

        @NotBlank
        @Size(min = 1, max = 10)
        String nickname,

        @NotNull
        Boolean marketingEmailOptIn
) {
}
