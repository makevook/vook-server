package vook.server.api.web.user.reqres;

import lombok.Data;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.usecases.user.OnboardingUserUseCase;

@Data
public class UserOnboardingRequest {

    public Funnel funnel;
    public Job job;

    public OnboardingUserUseCase.Command toCommand(String uid) {
        return new OnboardingUserUseCase.Command(uid, funnel, job);
    }
}
