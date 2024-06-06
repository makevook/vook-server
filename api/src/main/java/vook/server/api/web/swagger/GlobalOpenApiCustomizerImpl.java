package vook.server.api.web.swagger;

import io.swagger.v3.oas.models.OpenAPI;
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
                        .addProperty("code", new StringSchema().description("결과 코드")).addRequiredItem("code")
                        .addProperty("result", new Schema<>())
                );
    }
}
