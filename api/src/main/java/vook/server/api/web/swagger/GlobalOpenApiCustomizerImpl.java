package vook.server.api.web.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;

import java.util.Map;

public class GlobalOpenApiCustomizerImpl implements GlobalOpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        applyCommonApiResponseSchema(openApi);
    }

    private static void applyCommonApiResponseSchema(OpenAPI openApi) {
        openApi.getComponents()
                .addSchemas("CommonApiResponse", new Schema<Map<String, Object>>()
                        .addProperty("code", new IntegerSchema().description("응답 코드"))
                        .addProperty("message", new StringSchema().description("응답 메시지"))
                );
    }
}
