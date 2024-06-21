package vook.server.api.app.contexts.user.application.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vook.server.api.app.contexts.user.domain.Funnel;
import vook.server.api.app.contexts.user.domain.Job;

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
