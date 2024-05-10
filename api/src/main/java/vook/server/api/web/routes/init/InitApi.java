package vook.server.api.web.routes.init;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "init", description = "초기화 API")
public interface InitApi {

    @Operation(summary = "DB, Meilisearch 데이터 초기화")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공"
            ),
    })
    void init();
}
