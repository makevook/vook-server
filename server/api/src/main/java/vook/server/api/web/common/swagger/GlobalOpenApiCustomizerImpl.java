package vook.server.api.web.common.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import vook.server.api.web.common.response.ApiResponseCode;

public class GlobalOpenApiCustomizerImpl implements GlobalOpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        applyCommonApiResponseSchema(openApi);
    }

    private static void applyCommonApiResponseSchema(OpenAPI openApi) {
        openApi.getComponents()
                .addExamples(getKey(ComponentRefConsts.Example.SUCCESS), new Example()
                        .description("성공")
                        .value(String.format("""
                                {
                                    "code": "%s"
                                }""", ApiResponseCode.Ok.SUCCESS.code()))
                )
                .addExamples(getKey(ComponentRefConsts.Example.INVALID_PARAMETER), new Example()
                        .description("유효하지 않은 파라미터")
                        .value(String.format("""
                                {
                                    "code": "%s"
                                }""", ApiResponseCode.BadRequest.INVALID_PARAMETER.code()))
                )
                .addExamples(getKey(ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE), new Example()
                        .description("비즈니스 규칙 위반")
                        .value(String.format("""
                                {
                                    "code": "%s",
                                    "result": "규칙 위반 내용"
                                }""", ApiResponseCode.BadRequest.VIOLATION_BUSINESS_RULE.code()))
                )
                .addExamples(getKey(ComponentRefConsts.Example.UNHANDLED_ERROR), new Example()
                        .description("처리되지 않은 서버 에러")
                        .value(String.format("""
                                {
                                    "code": "%s"
                                }""", ApiResponseCode.ServerError.UNHANDLED_ERROR.code()))
                );
    }

    private static String getKey(String refString) {
        return refString.substring(refString.lastIndexOf('/') + 1);
    }
}
