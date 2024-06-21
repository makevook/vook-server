package vook.server.api.common.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiResponse<T> {

    @Schema(description = "결과 코드", requiredMode = Schema.RequiredMode.REQUIRED, example = "SUCCESS")
    private String code;
    private T result;

    public static <T> CommonApiResponse<T> ok() {
        return noResult(ApiResponseCode.Ok.SUCCESS);
    }

    public static <T> CommonApiResponse<T> okWithResult(T result) {
        CommonApiResponse<T> response = ok();
        response.result = result;
        return response;
    }

    public static <T> CommonApiResponse<T> noResult(ApiResponseCode code) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.code = code.code();
        return response;
    }

    public static <T> CommonApiResponse<T> withResult(ApiResponseCode code, T result) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.code = code.code();
        response.result = result;
        return response;
    }
}
