package vook.server.api.app.domain.user.exception;

import vook.server.api.common.exception.AppException;

public class NotReadyToOnboardingException extends AppException {

    @Override
    public String contents() {
        return "NotReadyToOnboarding";
    }
}
