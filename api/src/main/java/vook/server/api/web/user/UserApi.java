package vook.server.api.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.common.swagger.ComponentRefConsts;
import vook.server.api.web.user.reqres.UserInfoResponse;
import vook.server.api.web.user.reqres.UserOnboardingRequest;
import vook.server.api.web.user.reqres.UserRegisterRequest;
import vook.server.api.web.user.reqres.UserUpdateInfoRequest;

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
                            schema = @Schema(implementation = UserApiUserInfoResponse.class)
                    )
            ),
    })
    CommonApiResponse<UserInfoResponse> userInfo(VookLoginUser user);

    class UserApiUserInfoResponse extends CommonApiResponse<UserInfoResponse> {
    }

    @Operation(
            summary = "회원 가입",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 비즈니스 규칙 위반 내용
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
            summary = "온보딩",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 호출 시나리오
                    - 회원가입이 완료된 유저가 온보딩 프로세스의 마지막 페이지에서 '시작하기' 버튼을 클릭했을 때 호출됩니다.
                    - '건너뛰기' 버튼을 클릭 할 경우 해당 온보딩 프로세스 페이지의 선택 값을 null로 합니다.
                    - 온보딩 프로세스의 마지막 페이지에서 '건너뛰기' 버튼을 클릭했을 때도 이 API를 호출합니다. 
                                        
                    ## 비즈니스 규칙 위반 내용
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
                    ## 비즈니스 규칙 위반 내용
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

    @Operation(
            summary = "회원 재가입",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            },
            description = """
                    ## 수행 내용
                    - 유저의 상태를 탈퇴에서 가입으로 변경하고, 과거 온보딩 정보를 삭제합니다.
                                        
                    ## 호출 시나리오
                    - 탈퇴한 유저가 가입 페이지에서 '가입' 버튼을 클릭했을 때 호출됩니다.
                    - 이 API를 호출 한 후, 온보딩 API를 호출하여 온보딩을 다시 진행해야 합니다.
                                        
                    ## 비즈니스 규칙 위반 내용
                    - NotWithdrawnUser: 탈퇴하지 않은 유저가 해당 API를 호출 할 경우
                    """
    )
    CommonApiResponse<Void> reRegister(VookLoginUser user, UserRegisterRequest request);
}
