package vook.server.api.web.user.reqres;

import lombok.Data;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.domain.user.service.data.OnboardingCommand;

@Data
public class UserOnboardingRequest {

    public Funnel funnel;
    public Job job;

    public OnboardingCommand toCommand(String uid) {
        return OnboardingCommand.of(uid, funnel, job);
    }
}
