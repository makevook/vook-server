package vook.server.api.app.common.exception;

import jakarta.validation.ConstraintViolationException;

public class ParameterValidateException extends AppException {

    private final ConstraintViolationException cause;

    public ParameterValidateException(ConstraintViolationException cause) {
        this.cause = cause;
    }

    @Override
    public String contents() {
        return "ParameterValidate;" + cause.getMessage();
    }
}
