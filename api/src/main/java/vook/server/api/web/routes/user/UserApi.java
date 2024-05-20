package vook.server.api.web.routes.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;

@Tag(name = "user", description = "사용자 관련 API")
public interface UserApi {

    @Operation(summary = "사용자 정보")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = UserApiUerInfoResponse.class)
                    )
            ),
    })
    CommonApiResponse<UserInfoResponse> userInfo(VookLoginUser user);

    class UserApiUerInfoResponse extends CommonApiResponse<UserInfoResponse> {
    }
}
