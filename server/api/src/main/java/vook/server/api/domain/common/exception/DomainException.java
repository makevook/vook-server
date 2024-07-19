package vook.server.api.domain.common.exception;

import lombok.NoArgsConstructor;
import vook.server.api.globalcommon.exception.AppException;

@NoArgsConstructor
public class DomainException extends AppException {
    
    public DomainException(RuntimeException cause) {
        super(cause);
    }
}
