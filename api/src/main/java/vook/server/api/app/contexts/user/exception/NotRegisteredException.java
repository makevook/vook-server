package vook.server.api.app.contexts.user.exception;

import vook.server.api.app.common.exception.AppException;

public class NotRegisteredException extends AppException {
    @Override
    public String contents() {
        return "NotRegistered";
    }
}
