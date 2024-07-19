package vook.server.api.web.user.reqres;

import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.web.user.usecase.OnboardingUserUseCase;

public record UserOnboardingRequest(
        Funnel funnel,
        Job job
) {
    public OnboardingUserUseCase.Command toCommand(String uid) {
        return new OnboardingUserUseCase.Command(uid, funnel, job);
    }
}
