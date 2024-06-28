package vook.server.api.domain.user.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;

@Builder
public record OnboardingCommand(
        @NotBlank
        String userUid,

        @NotNull
        Funnel funnel,

        @NotNull
        Job job
) {
}
