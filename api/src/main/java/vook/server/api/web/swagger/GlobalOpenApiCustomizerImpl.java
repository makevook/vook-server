package vook.server.api.web.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import vook.server.api.web.common.ApiResponseCode;

import java.util.Map;

public class GlobalOpenApiCustomizerImpl implements GlobalOpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        applyCommonApiResponseSchema(openApi);
    }

    private static void applyCommonApiResponseSchema(OpenAPI openApi) {
        openApi.getComponents()
                .addSchemas(getKey(ComponentRefConsts.Schema.COMMON_API_RESPONSE), new Schema<Map<String, Object>>()
                        .addProperty("code", new StringSchema().description("결과 코드")).addRequiredItem("code")
                        .addProperty("result", new Schema<>())
                )
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
