package vook.server.api.web.common;

public class CommonApiException {
    public static abstract class Exception extends RuntimeException {
        
        protected String code;

        public Exception(String code, Throwable cause) {
            super(code, cause);
            this.code = code;
        }

        abstract CommonApiResponse<?> response();

        abstract int statusCode();
    }

    public static class BadRequest extends Exception {

        public BadRequest(String code, Throwable cause) {
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

        public ServerError(String code, Throwable cause) {
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
