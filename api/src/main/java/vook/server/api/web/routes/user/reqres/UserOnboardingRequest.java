package vook.server.api.web.routes.user.reqres;

import lombok.Data;
import vook.server.api.app.contexts.user.domain.Funnel;
import vook.server.api.app.contexts.user.domain.Job;
import vook.server.api.app.contexts.user.application.data.OnboardingCommand;

@Data
public class UserOnboardingRequest {

    public Funnel funnel;
    public Job job;

    public OnboardingCommand toCommand(String uid) {
        return OnboardingCommand.of(uid, funnel, job);
    }
}
