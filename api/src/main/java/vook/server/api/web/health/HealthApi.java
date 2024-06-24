package vook.server.api.web.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "health")
public interface HealthApi {

    @Operation(
            summary = "서버 상태 확인",
            description = """
                    - 서버의 상태를 체크하는 API입니다.""")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(name = "성공", value = "OK")
            )
    )
    String health();
}
