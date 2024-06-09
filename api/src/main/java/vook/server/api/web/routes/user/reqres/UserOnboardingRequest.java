package vook.server.api.web.routes.user.reqres;

import lombok.Data;
import vook.server.api.app.user.data.OnboardingCommand;
import vook.server.api.model.user.Funnel;
import vook.server.api.model.user.Job;

@Data
public class UserOnboardingRequest {

    public Funnel funnel;
    public Job job;

    public OnboardingCommand toCommand(String uid) {
        return OnboardingCommand.of(uid, funnel, job);
    }
}
