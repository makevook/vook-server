package vook.server.api.web.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.user.reqres.UserInfoResponse;
import vook.server.api.web.user.reqres.UserOnboardingRequest;
import vook.server.api.web.user.reqres.UserRegisterRequest;
import vook.server.api.web.user.reqres.UserUpdateInfoRequest;
import vook.server.api.web.user.usecase.OnboardingUserUseCase;
import vook.server.api.web.user.usecase.WithdrawUserUseCase;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController implements UserApi {

    private final UserLogic userLogic;
    private final OnboardingUserUseCase onboardingUserUseCase;
    private final WithdrawUserUseCase withdrawUserUseCase;

    @Override
    @GetMapping("/info")
    public CommonApiResponse<UserInfoResponse> userInfo(
            @AuthenticationPrincipal VookLoginUser loginUser
    ) {
        User user = userLogic.getByUid(loginUser.getUid());
        UserInfoResponse response = UserInfoResponse.from(user);
        return CommonApiResponse.okWithResult(response);
    }

    @Override
    @PostMapping("/register")
    public CommonApiResponse<Void> register(
            @AuthenticationPrincipal VookLoginUser loginUser,
            @Validated @RequestBody UserRegisterRequest request
    ) {
        userLogic.register(request.toCommand(loginUser.getUid()));
        return CommonApiResponse.ok();
    }

    @Override
    @PostMapping("/onboarding")
    public CommonApiResponse<Void> onboarding(
            @AuthenticationPrincipal VookLoginUser loginUser,
            @RequestBody UserOnboardingRequest request
    ) {
        onboardingUserUseCase.execute(request.toCommand(loginUser.getUid()));
        return CommonApiResponse.ok();
    }

    @Override
    @PutMapping("/info")
    public CommonApiResponse<Void> updateInfo(
            @AuthenticationPrincipal VookLoginUser loginUser,
            @Validated @RequestBody UserUpdateInfoRequest request
    ) {
        userLogic.updateInfo(loginUser.getUid(), request.nickname().trim());
        return CommonApiResponse.ok();
    }

    @Override
    @PostMapping("/withdraw")
    public CommonApiResponse<Void> withdraw(
            @AuthenticationPrincipal VookLoginUser loginUser
    ) {
        withdrawUserUseCase.execute(new WithdrawUserUseCase.Command(loginUser.getUid()));
        return CommonApiResponse.ok();
    }

    @Override
    @PostMapping("/re-register")
    public CommonApiResponse<Void> reRegister(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody UserRegisterRequest request
    ) {
        userLogic.reRegister(request.toCommand(user.getUid()));
        return CommonApiResponse.ok();
    }
}
