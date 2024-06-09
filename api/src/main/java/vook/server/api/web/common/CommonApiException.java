package vook.server.api.web.common;

public class CommonApiException extends RuntimeException {

    private final ApiResponseCode code;
    private final int statusCode;

    CommonApiException(ApiResponseCode code, int statusCode, Throwable cause) {
        super(code.code(), cause);
        this.code = code;
        this.statusCode = statusCode;
    }

    public CommonApiResponse<?> response() {
        return CommonApiResponse.noResult(code);
    }

    public int statusCode() {
        return statusCode;
    }

    public static CommonApiException badRequest(ApiResponseCode code, Throwable cause) {
        return new CommonApiException(code, 400, cause);
    }

    public static CommonApiException serverError(ApiResponseCode code, Throwable cause) {
        return new CommonApiException(code, 500, cause);
    }
}
