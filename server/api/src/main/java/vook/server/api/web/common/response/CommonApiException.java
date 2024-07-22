package vook.server.api.web.common.response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonApiException extends RuntimeException {

    private final ApiResponseCode code;
    private final int statusCode;
    private final String message;

    CommonApiException(ApiResponseCode code, int statusCode, Throwable cause) {
        this(code, statusCode, cause, null);
    }

    CommonApiException(ApiResponseCode code, int statusCode, Throwable cause, String message) {
        super(code.code(), cause);
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    public CommonApiResponse<?> response() {
        if (message == null) {
            return CommonApiResponse.noResult(code);
        } else {
            return CommonApiResponse.withResult(code, message);
        }
    }

    public int statusCode() {
        return statusCode;
    }

    public static CommonApiException badRequest(ApiResponseCode code, Throwable cause) {
        return new CommonApiException(code, 400, cause);
    }

    public static CommonApiException badRequest(ApiResponseCode code, Throwable cause, String message) {
        return new CommonApiException(code, 400, cause, message);
    }

    public static CommonApiException serverError(ApiResponseCode code, Throwable cause) {
        return new CommonApiException(code, 500, cause);
    }
}
