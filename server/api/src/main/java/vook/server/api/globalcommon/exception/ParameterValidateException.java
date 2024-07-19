package vook.server.api.globalcommon.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParameterValidateException extends AppException {
    
    public ParameterValidateException(ConstraintViolationException cause) {
        super(cause);
    }
}
