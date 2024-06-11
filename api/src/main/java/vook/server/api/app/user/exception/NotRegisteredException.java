package vook.server.api.app.user.exception;

import vook.server.api.app.common.AppException;

public class NotRegisteredException extends AppException {
    @Override
    public String contents() {
        return "NotRegistered";
    }
}
