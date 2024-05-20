package vook.server.api.web.routes.user.reqres;

import lombok.Data;
import vook.server.api.app.user.data.CompleteOnboardingCommand;

@Data
public class UserOnboardingCompleteRequest {

    public String funnel;
    public String job;

    public CompleteOnboardingCommand toCommand(String uid) {
        return CompleteOnboardingCommand.of(uid, funnel, job);
    }
}
