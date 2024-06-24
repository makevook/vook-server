package vook.server.api.web.common.swagger;

/**
 * OpenAPI ComponentRef 상수
 */
public class ComponentRefConsts {

    public static class Schema {
        public static final String COMMON_API_RESPONSE = "#/components/schemas/CommonApiResponse";
    }

    public static class Example {
        public static final String SUCCESS = "#/components/examples/Success";
        public static final String INVALID_PARAMETER = "#/components/examples/InvalidParameter";
        public static final String UNHANDLED_ERROR = "#/components/examples/UnhandledError";
        public static final String VIOLATION_BUSINESS_RULE = "#/components/examples/ViolationBusinessRule";
    }
}
