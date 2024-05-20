package vook.server.api.web.routes.init;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "init", description = "초기화 API")
public interface InitApi {

    @Operation(
            summary = "데이터 초기화",
            description = "모든 데이터를 삭제하고, 데모용 데이터를 생성시킨 상태로 초기화 시킵니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공"
            ),
    })
    void init();
}
