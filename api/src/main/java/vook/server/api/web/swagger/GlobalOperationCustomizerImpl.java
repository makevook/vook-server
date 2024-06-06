package vook.server.api.web.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.web.method.HandlerMethod;
import vook.server.api.web.common.CommonApiResponse;

import java.util.HashMap;

public class GlobalOperationCustomizerImpl implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        applyInternalServerErrorApiResponse(operation);
        return operation;
    }

    private static void applyInternalServerErrorApiResponse(Operation operation) {
        MediaType jsonType = prepareOrGetJsonMediaType(operation);

        if (jsonType.getSchema() == null) {
            jsonType.setSchema(new Schema<CommonApiResponse>().$ref("#/components/schemas/CommonApiResponse"));
        }

        jsonType.getExamples().put("처리되지 않은 서버 에러",
                new Example()
                        .description("처리되지 않은 서버 에러")
                        .value("""
                                {
                                    "code": "UNHANDLED_ERROR"
                                }""")
        );
    }

    private static MediaType prepareOrGetJsonMediaType(Operation operation) {
        ApiResponse apiResponse = operation.getResponses().computeIfAbsent("500", k -> new ApiResponse());

        if (apiResponse.getContent() == null) {
            apiResponse.setContent(new Content());
        }

        MediaType jsonType = apiResponse.getContent().computeIfAbsent("application/json", k -> new MediaType());

        if (jsonType.getExamples() == null) {
            jsonType.setExamples(new HashMap<>());
        }

        return jsonType;
    }
}
