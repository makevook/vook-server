package vook.server.api.app.contexts.user.exception;

import vook.server.api.common.exception.AppException;

public class AlreadyOnboardingException extends AppException {

    @Override
    public String contents() {
        return "AlreadyOnboarding";
    }
}
