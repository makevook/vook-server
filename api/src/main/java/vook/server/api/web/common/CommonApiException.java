package vook.server.api.web.common;

public class CommonApiException {
    public static abstract class Exception extends RuntimeException {

        protected ApiResponseCode code;

        public Exception(ApiResponseCode code, Throwable cause) {
            super(code.code(), cause);
            this.code = code;
        }

        abstract CommonApiResponse<?> response();

        abstract int statusCode();
    }

    public static class BadRequest extends Exception {

        public BadRequest(ApiResponseCode code, Throwable cause) {
            super(code, cause);
        }

        @Override
        public CommonApiResponse<?> response() {
            return CommonApiResponse.noResult(code);
        }

        @Override
        int statusCode() {
            return 400;
        }
    }

    public static class ServerError extends Exception {

        public ServerError(ApiResponseCode code, Throwable cause) {
            super(code, cause);
        }

        @Override
        public CommonApiResponse<?> response() {
            return CommonApiResponse.noResult(code);
        }

        @Override
        int statusCode() {
            return 500;
        }
    }
}
