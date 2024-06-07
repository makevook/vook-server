package vook.server.api.web.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;

public class GlobalOperationCustomizerImpl implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        applyDefaultOkApiResponse(operation);
        applyInternalServerErrorApiResponse(operation);
        return operation;
    }

    private void applyDefaultOkApiResponse(Operation operation) {
        ApiResponse apiResponse = operation.getResponses().computeIfAbsent(
                "200",
                k -> new ApiResponse().description("OK")
        );

        if (apiResponse.getContent() == null) {
            apiResponse.setContent(new Content());
        } else {
            // SpringDoc이 기본으로 제공하는 Content 제거
            apiResponse.getContent().remove("*/*");
        }

        // Content가 존재하면 종료
        if (!apiResponse.getContent().isEmpty()) {
            return;
        }

        // Content가 존재하지 않으면 기본 성공 응답 추가
        apiResponse.getContent().computeIfAbsent("application/json", k -> new MediaType()
                .schema(new Schema<>().$ref(ComponentRefConsts.Schema.COMMON_API_RESPONSE))
                .addExamples("성공", new Example().$ref(ComponentRefConsts.Example.SUCCESS)));
    }

    private static void applyInternalServerErrorApiResponse(Operation operation) {
        MediaType jsonType = prepareOrGetJsonMediaType(operation);

        if (jsonType.getSchema() == null) {
            jsonType.setSchema(new Schema<>().$ref(ComponentRefConsts.Schema.COMMON_API_RESPONSE));
        }

        jsonType.getExamples().put("처리되지 않은 서버 에러", new Example().$ref(ComponentRefConsts.Example.UNHANDLED_ERROR));
    }

    private static MediaType prepareOrGetJsonMediaType(Operation operation) {
        ApiResponse apiResponse = operation.getResponses().computeIfAbsent(
                "500",
                k -> new ApiResponse().description("Internal Server Error")
        );

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
