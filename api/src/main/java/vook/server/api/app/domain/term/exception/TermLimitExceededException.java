package vook.server.api.app.domain.term.exception;

import vook.server.api.app.common.AppException;

public class TermLimitExceededException extends AppException {
    
    @Override
    public String contents() {
        return "TermLimitExceeded";
    }
}
