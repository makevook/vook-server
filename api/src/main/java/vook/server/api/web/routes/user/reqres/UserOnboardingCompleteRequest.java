package vook.server.api.web.routes.user.reqres;

import lombok.Data;
import vook.server.api.app.user.data.CompleteOnboardingCommand;
import vook.server.api.model.user.Funnel;
import vook.server.api.model.user.Job;

@Data
public class UserOnboardingCompleteRequest {

    public Funnel funnel;
    public Job job;

    public CompleteOnboardingCommand toCommand(String uid) {
        return CompleteOnboardingCommand.of(uid, funnel, job);
    }
}
