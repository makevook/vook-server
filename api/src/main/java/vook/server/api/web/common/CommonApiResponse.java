package vook.server.api.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiResponse<T> {

    @Schema(description = "결과 코드", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    private T result;

    public static <T> CommonApiResponse<T> ok() {
        return noResult("success");
    }

    public static <T> CommonApiResponse<T> okWithResult(T result) {
        CommonApiResponse<T> response = ok();
        response.result = result;
        return response;
    }

    public static <T> CommonApiResponse<T> noResult(String code) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.code = code;
        return response;
    }

    public static <T> CommonApiResponse<T> withResult(String code, T result) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.code = code;
        response.result = result;
        return response;
    }
}
