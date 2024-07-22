package vook.server.api.globalcommon.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class AppException extends RuntimeException {
    
    public AppException(RuntimeException cause) {
        super(cause);
    }
}
