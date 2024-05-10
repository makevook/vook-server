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
        public BadRequest(String message, Throwable cause) {
            super(message, cause);
        }

        public BadRequest(String message) {
            super(message);
        }

        @Override
        public CommonApiResponse<?> response() {
            return CommonApiResponse.noResult(400, message);
        }
    }

    public static class ServerError extends Exception {
        public ServerError(Throwable cause) {
            super(cause);
        }

        @Override
        public CommonApiResponse<?> response() {
            return CommonApiResponse.noResult(500, "처리되지 않은 서버 에러가 발생하였습니다.");
        }
    }
}
