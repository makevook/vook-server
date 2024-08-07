package vook.server.api.globalcommon.helper.jwt;

import lombok.Getter;

@Getter
public class JWTException extends RuntimeException {

    private final String useData;

    public JWTException(Throwable cause) {
        super(cause);
        this.useData = "";
    }

    public JWTException(Throwable cause, String useData) {
        super(cause);
        this.useData = useData;
    }

    @Override
    public String getMessage() {
        if (useData.isEmpty()) {
            return super.getMessage();
        } else {
            return super.getMessage() + "; useData(" + useData + ")";
        }
    }
}
