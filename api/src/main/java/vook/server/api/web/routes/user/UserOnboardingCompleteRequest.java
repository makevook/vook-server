package vook.server.api.web.routes.user;

import lombok.Data;
import vook.server.api.app.user.CompleteOnboardingCommand;

@Data
public class UserOnboardingCompleteRequest {

    public String funnel;
    public String job;

    public CompleteOnboardingCommand toCommand(String uid) {
        return CompleteOnboardingCommand.of(uid, funnel, job);
    }
}
