package vook.server.api.web.user.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateInfoRequest {

    @NotBlank
    @Size(min = 1, max = 10)
    private String nickname;
}
