package vook.server.api.web.common;

public class CommonApiException {
    public static abstract class Exception extends RuntimeException {
        protected String message;

        public Exception(Throwable cause) {
            super(cause);
            this.message = cause.getMessage();
        }

        public Exception(String message) {
            super(message);
            this.message = message;
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
            this.message = message;
        }

        abstract CommonApiResponse<?> response();
    }

    public static class BadRequest extends Exception {

        private static final int STATUS_CODE = 400;

        public BadRequest(String message, Throwable cause) {
            super(message, cause);
        }

        public BadRequest(String message) {
            super(message);
        }

        @Override
        public CommonApiResponse<?> response() {
            return CommonApiResponse.noResult(STATUS_CODE, message);
        }
    }

    public static class ServerError extends Exception {

        private static final int STATUS_CODE = 500;

        public ServerError(String message, Throwable cause) {
            super(message, cause);
        }

        public ServerError(Throwable cause) {
            super(cause);
        }

        @Override
        public CommonApiResponse<?> response() {
            return CommonApiResponse.noResult(STATUS_CODE, message);
        }

        public CommonApiResponse<?> response(String message) {
            return CommonApiResponse.noResult(STATUS_CODE, message);
        }
    }
}
