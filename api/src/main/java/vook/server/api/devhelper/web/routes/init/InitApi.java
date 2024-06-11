package vook.server.api.devhelper.web.routes.init;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.CommonApiResponse;

@Tag(name = "init", description = "초기화 API")
public interface InitApi {

    @Operation(
            summary = "데이터 초기화",
            description = "모든 데이터를 삭제하고, 데모용 데이터를 생성시킨 상태로 초기화 시킵니다."
    )
    CommonApiResponse<Void> init();
}
