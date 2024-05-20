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

import java.util.List;

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
            summary = "약관 목록",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = UserApiTermsResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<UserTermsResponse>> terms();

    class UserApiTermsResponse extends CommonApiResponse<List<UserTermsResponse>> {
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
}
