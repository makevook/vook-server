package vook.server.api.web.routes.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.user.reqres.UserInfoResponse;
import vook.server.api.web.routes.user.reqres.UserOnboardingRequest;
import vook.server.api.web.routes.user.reqres.UserRegisterRequest;
import vook.server.api.web.swagger.ComponentRefConsts;

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
                    content = @Content(
                            mediaType = "application/json",
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
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - AlreadyRegistered: 이미 회원가입이 완료된 유저가 해당 API를 호출 할 경우
                    - WithdrawnUser: 탈퇴한 유저가 해당 API를 호출 할 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = ComponentRefConsts.Schema.COMMON_API_RESPONSE),
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 파라미터", ref = ComponentRefConsts.Example.INVALID_PARAMETER),
                                    @ExampleObject(name = "비즈니스 규칙 위반", ref = ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE)
                            }
                    )
            ),
    })
    CommonApiResponse<Void> register(VookLoginUser user, UserRegisterRequest request);

    @Operation(
            summary = "온보딩 완료",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - NotReadyToOnboarding: 회원 가입이 완료되지 않은 유저가 해당 API를 호출 할 경우
                    - AlreadyOnboarding: 이미 온보딩이 완료된 유저가 해당 API를 호출 할 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = ComponentRefConsts.Schema.COMMON_API_RESPONSE),
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 파라미터", ref = ComponentRefConsts.Example.INVALID_PARAMETER),
                                    @ExampleObject(name = "비즈니스 규칙 위반", ref = ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE)
                            }
                    )
            ),
    })
    CommonApiResponse<Void> onboarding(VookLoginUser user, UserOnboardingRequest request);

    @Operation(
            summary = "사용자 정보 수정",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    비즈니스 규칙 위반 내용
                    - NotRegistered: 가입하지 않은 유저가 해당 API를 호출 할 경우"""
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(ref = ComponentRefConsts.Schema.COMMON_API_RESPONSE),
                            examples = {
                                    @ExampleObject(name = "유효하지 않은 파라미터", ref = ComponentRefConsts.Example.INVALID_PARAMETER),
                                    @ExampleObject(name = "비즈니스 규칙 위반", ref = ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE)
                            }
                    )
            ),
    })
    CommonApiResponse<Void> updateInfo(VookLoginUser user, UserUpdateInfoRequest request);

    @Operation(
            summary = "회원 탈퇴",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = "탈퇴된 회원에 대한 요청은 무시됩니다."
    )
    CommonApiResponse<Void> withdraw(VookLoginUser user);
}
