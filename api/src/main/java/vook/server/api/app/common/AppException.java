package vook.server.api.app.common;

import lombok.Getter;

@Getter
public abstract class AppException extends RuntimeException {
    public abstract String contents();
}
