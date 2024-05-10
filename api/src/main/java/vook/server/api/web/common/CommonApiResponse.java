package vook.server.api.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonApiResponse<T> {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
}
