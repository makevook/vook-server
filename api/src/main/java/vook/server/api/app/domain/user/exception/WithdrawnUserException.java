package vook.server.api.app.domain.user.exception;

import vook.server.api.app.common.AppException;

public class WithdrawnUserException extends AppException {
    @Override
    public String contents() {
        return "WithdrawnUser";
    }
}
