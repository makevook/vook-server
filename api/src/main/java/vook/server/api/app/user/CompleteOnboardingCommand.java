package vook.server.api.app.user;

import lombok.Getter;

@Getter
public class CompleteOnboardingCommand {

    public String userUid;
    public String funnel;
    public String job;

    public static CompleteOnboardingCommand of(
            String userUid,
            String funnel,
            String job
    ) {
        CompleteOnboardingCommand command = new CompleteOnboardingCommand();
        command.userUid = userUid;
        command.funnel = funnel;
        command.job = job;
        return command;
    }
}
