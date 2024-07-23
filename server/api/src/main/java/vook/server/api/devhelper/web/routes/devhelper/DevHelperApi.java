package vook.server.api.devhelper.web.routes.devhelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.response.CommonApiResponse;

@Tag(name = "dev-helper", description = "개발 지원용 API")
public interface DevHelperApi {

    @Operation(
            summary = "데이터 초기화",
            description = "모든 유저 데이터를 삭제한다."
    )
    CommonApiResponse<Void> init();

    @Operation(
            summary = "데이터 동기화",
            description = "검색 엔진의 기존 데이터를 삭제하고, DB의 데이터를 바탕으로 새로 생성합니다."
    )
    CommonApiResponse<Void> sync();
}
