package vook.server.api.web.routes.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.user.reqres.UserInfoResponse;
import vook.server.api.web.routes.user.reqres.UserOnboardingCompleteRequest;
import vook.server.api.web.routes.user.reqres.UserRegisterRequest;

@Tag(name = "user", description = "사용자 관련 API")
public interface UserApi {

    @Operation(
            summary = "사용자 정보",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            }
    )
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

    @Operation(
            summary = "회원가입",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공"
            ),
    })
    CommonApiResponse<Void> register(VookLoginUser user, UserRegisterRequest request);

    @Operation(
            summary = "온보딩 완료",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공"
            ),
    })
    CommonApiResponse<Void> onboardingComplete(VookLoginUser user, UserOnboardingCompleteRequest request);
}
