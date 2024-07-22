package vook.server.api.domain.user.logic;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import vook.server.api.domain.user.model.user_info.Funnel;
import vook.server.api.domain.user.model.user_info.Job;

@Builder
public record UserOnboardingCommand(
        @NotBlank
        String userUid,

        Funnel funnel,
        Job job
) {
}
