package vook.server.api.web.routes.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.user.reqres.UserInfoResponse;
import vook.server.api.web.routes.user.reqres.UserOnboardingRequest;
import vook.server.api.web.routes.user.reqres.UserRegisterRequest;
import vook.server.api.web.routes.user.reqres.UserUpdateInfoRequest;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController implements UserApi {

    private final UserWebService service;

    @Override
    @GetMapping("/info")
    public CommonApiResponse<UserInfoResponse> userInfo(
            @AuthenticationPrincipal VookLoginUser user
    ) {
        UserInfoResponse response = service.userInfo(user);
        return CommonApiResponse.okWithResult(response);
    }

    @Override
    @PostMapping("/register")
    public CommonApiResponse<Void> register(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody UserRegisterRequest request
    ) {
        service.register(user, request);
        return CommonApiResponse.ok();
    }

    @Override
    @PostMapping("/onboarding")
    public CommonApiResponse<Void> onboarding(
            @AuthenticationPrincipal VookLoginUser user,
            @RequestBody UserOnboardingRequest request
    ) {
        service.onboarding(user, request);
        return CommonApiResponse.ok();
    }

    @Override
    @PutMapping("/info")
    public CommonApiResponse<Void> updateInfo(
            @AuthenticationPrincipal VookLoginUser user,
            @Validated @RequestBody UserUpdateInfoRequest request
    ) {
        service.updateInfo(user, request);
        return CommonApiResponse.ok();
    }

    @Override
    @PostMapping("/withdraw")
    public CommonApiResponse<Void> withdraw(
            @AuthenticationPrincipal VookLoginUser user
    ) {
        service.withdraw(user);
        return CommonApiResponse.ok();
    }
}
