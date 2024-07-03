package vook.server.api.domain.user.service.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;

@Builder
public record UserOnboardingCommand(
        @NotBlank
        String userUid,

        Funnel funnel,
        Job job
) {
}
