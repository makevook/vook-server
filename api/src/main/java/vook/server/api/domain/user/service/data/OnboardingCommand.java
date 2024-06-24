package vook.server.api.domain.user.service.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;

@Getter
public class OnboardingCommand {

    @NotBlank
    public String userUid;

    @NotNull
    public Funnel funnel;

    @NotNull
    public Job job;

    public static OnboardingCommand of(
            String userUid,
            Funnel funnel,
            Job job
    ) {
        OnboardingCommand command = new OnboardingCommand();
        command.userUid = userUid;
        command.funnel = funnel;
        command.job = job;
        return command;
    }
}
