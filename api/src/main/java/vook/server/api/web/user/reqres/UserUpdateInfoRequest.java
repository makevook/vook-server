package vook.server.api.web.user.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateInfoRequest(
        @NotBlank
        @Size(min = 1, max = 10)
        String nickname
) {
}
