package vook.server.api.web.common.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.GlobalOperationCustomizer;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;
import org.springframework.web.method.HandlerMethod;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.common.swagger.annotation.IncludeBadRequestResponse;
import vook.server.api.web.common.swagger.annotation.IncludeOkResponse;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class GlobalOperationCustomizerImpl implements GlobalOperationCustomizer {

    private final Supplier<OpenAPI> openAPISupplier;

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        applyOkApiResponse(operation, handlerMethod); //200
        applyBadRequestApiResponse(operation, handlerMethod); //400
        applyUnauthorizedApiResponse(operation); //401
        applyInternalServerErrorApiResponse(operation); //500
        return operation;
    }

    private void applyOkApiResponse(Operation operation, HandlerMethod handlerMethod) {
        IncludeOkResponse methodAnnotation = handlerMethod.getMethodAnnotation(IncludeOkResponse.class);
        if (methodAnnotation == null) {
            return;
        }

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

        Class<?> type = methodAnnotation.implementation();
        if (type == null || type == Void.class) {
            // Content가 존재하지 않으면 기본 성공 응답 추가
            apiResponse.getContent().computeIfAbsent("application/json", k -> new MediaType()
                    .schema(SpringDocAnnotationsUtils.resolveSchemaFromType(CommonApiResponse.class, openAPISupplier.get().getComponents(), null))
                    .addExamples("성공", new Example().$ref(ComponentRefConsts.Example.SUCCESS)));
        } else {
            Schema schema = SpringDocAnnotationsUtils.resolveSchemaFromType(type, openAPISupplier.get().getComponents(), null);
            apiResponse.getContent().computeIfAbsent("application/json", k -> new MediaType().schema(schema));
        }
    }

    private void applyBadRequestApiResponse(Operation operation, HandlerMethod handlerMethod) {
        IncludeBadRequestResponse methodAnnotation = handlerMethod.getMethodAnnotation(IncludeBadRequestResponse.class);
        if (methodAnnotation == null) {
            return;
        }

        ApiResponse apiResponse = operation.getResponses().computeIfAbsent(
                "400",
                k -> new ApiResponse().description("Bad Request")
        );

        if (apiResponse.getContent() == null) {
            apiResponse.setContent(new Content());
        }

        MediaType jsonType = apiResponse.getContent().computeIfAbsent("application/json", k -> new MediaType());

        if (jsonType.getSchema() == null) {
            jsonType.setSchema(getCommonApiResponse());
        }

        if (jsonType.getExamples() == null) {
            jsonType.setExamples(new HashMap<>());
        }

        for (IncludeBadRequestResponse.Kind kind : methodAnnotation.value()) {
            kind.applyExample(jsonType);
        }
    }

    private void applyUnauthorizedApiResponse(Operation operation) {
        List<SecurityRequirement> security = operation.getSecurity();
        if (security == null) {
            return;
        }

        security.forEach(sr -> {
            if (sr.containsKey("AccessToken")) {
                operation.getResponses().computeIfAbsent(
                        "401",
                        k -> new ApiResponse().description("Unauthorized")
                );
            }
        });
    }

    private void applyInternalServerErrorApiResponse(Operation operation) {
        ApiResponse apiResponse = operation.getResponses().computeIfAbsent(
                "500",
                k -> new ApiResponse().description("Internal Server Error")
        );

        if (apiResponse.getContent() == null) {
            apiResponse.setContent(new Content());
        }

        MediaType jsonType = apiResponse.getContent().computeIfAbsent("application/json", k -> new MediaType());

        if (jsonType.getSchema() == null) {
            jsonType.setSchema(getCommonApiResponse());
        }

        if (jsonType.getExamples() == null) {
            jsonType.setExamples(new HashMap<>());
        }

        jsonType.getExamples().put("처리되지 않은 서버 에러", new Example().$ref(ComponentRefConsts.Example.UNHANDLED_ERROR));
    }

    private Schema getCommonApiResponse() {
        return SpringDocAnnotationsUtils.resolveSchemaFromType(CommonApiResponse.class, openAPISupplier.get().getComponents(), null);
    }

}
