package vook.server.api.common.exception;

import lombok.Getter;

@Getter
public abstract class AppException extends RuntimeException {
    public abstract String contents();
}
