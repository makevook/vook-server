package vook.server.api.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springframework.web.method.HandlerMethod;
import vook.server.api.web.common.CommonApiResponse;

import java.util.Map;

public class GlobalOperationCustomizerImpl implements GlobalOperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        applyInternalServerErrorApiResponse(operation);
        return operation;
    }

    private static void applyInternalServerErrorApiResponse(Operation operation) {
        ApiResponse internalServerErrorApiResponse = new ApiResponse()
                .description("처리되지 않은 서버 에러")
                .content(new Content().addMediaType(
                        "application/json",
                        new MediaType()
                                .schema(new Schema<CommonApiResponse>().$ref("#/components/schemas/CommonApiResponse"))
                                .examples(Map.of("서버 에러", new Example().value(
                                        """
                                                {
                                                    "code": 500, 
                                                    "message": "처리되지 않은 서버 에러가 발생하였습니다."
                                                }"""
                                )))
                ));

        operation.getResponses().addApiResponse("500", internalServerErrorApiResponse);
    }
}
