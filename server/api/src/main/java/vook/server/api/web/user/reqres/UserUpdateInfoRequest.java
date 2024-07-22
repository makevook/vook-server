package vook.server.api.web.user.reqres;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateInfoRequest(
        @NotBlank
        String nickname
) {
}
