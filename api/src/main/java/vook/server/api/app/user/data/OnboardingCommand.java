package vook.server.api.app.user.data;

import lombok.Getter;
import vook.server.api.model.user.Funnel;
import vook.server.api.model.user.Job;

@Getter
public class OnboardingCommand {

    public String userUid;
    public Funnel funnel;
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
