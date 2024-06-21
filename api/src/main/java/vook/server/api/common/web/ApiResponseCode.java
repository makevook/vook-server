package vook.server.api.common.web;

public interface ApiResponseCode {

    String code();

    enum Ok implements ApiResponseCode {

        SUCCESS;

        @Override
        public String code() {
            return this.name();
        }
    }

    enum BadRequest implements ApiResponseCode {

        INVALID_PARAMETER,
        VIOLATION_BUSINESS_RULE,
        ;

        @Override
        public String code() {
            return this.name();
        }
    }

    enum ServerError implements ApiResponseCode {

        UNHANDLED_ERROR;

        @Override
        public String code() {
            return this.name();
        }
    }
}
