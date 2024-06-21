package vook.server.api.web.routes.user.reqres;

import lombok.Data;
import vook.server.api.app.context.user.data.OnboardingCommand;
import vook.server.api.app.context.user.domain.Funnel;
import vook.server.api.app.context.user.domain.Job;

@Data
public class UserOnboardingRequest {

    public Funnel funnel;
    public Job job;

    public OnboardingCommand toCommand(String uid) {
        return OnboardingCommand.of(uid, funnel, job);
    }
}
