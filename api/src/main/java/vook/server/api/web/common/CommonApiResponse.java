package vook.server.api.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiResponse<T> {

    @Schema(description = "응답 코드", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;
    @Schema(description = "응답 메시지", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    private T result;

    public static <T> CommonApiResponse<T> ok() {
        return noResult(200, "API 요청이 성공했습니다.");
    }

    public static <T> CommonApiResponse<T> okWithResult(T result) {
        CommonApiResponse<T> response = ok();
        response.result = result;
        return response;
    }

    public static <T> CommonApiResponse<T> noResult(Integer code, String message) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> CommonApiResponse<T> withResult(Integer code, String message, T result) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.code = code;
        response.message = message;
        response.result = result;
        return response;
    }
}
