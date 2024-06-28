package vook.server.api.web.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.usecases.user.OnboardingUserUseCase;
import vook.server.api.usecases.user.WithdrawUserUseCase;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.common.response.CommonApiResponse;
import vook.server.api.web.user.reqres.UserInfoResponse;
import vook.server.api.web.user.reqres.UserOnboardingRequest;
import vook.server.api.web.user.reqres.UserRegisterRequest;
import vook.server.api.web.user.reqres.UserUpdateInfoRequest;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController implements UserApi {

    private final UserService userService;
    private final OnboardingUserUseCase onboardingUserUseCase;
    private final WithdrawUserUseCase withdrawUserUseCase;

    @Override
    @GetMapping("/info")
    public CommonApiResponse<UserInfoResponse> userInfo(
            @AuthenticationPrincipal VookLoginUser loginUser
    ) {
        User user = userService.getByUid(loginUser.getUid());
        UserInfoResponse response = UserInfoResponse.from(user);
        return CommonApiResponse.okWithResult(response);
    }

    @Override
    @PostMapping("/register")
    public CommonApiResponse<Void> register(
            @AuthenticationPrincipal VookLoginUser loginUser,
            @Validated @RequestBody UserRegisterRequest request
    ) {
        userService.register(request.toCommand(loginUser.getUid()));
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
        userService.updateInfo(loginUser.getUid(), request.nickname());
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
}
