package vook.server.api.web.routes.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.user.reqres.UserInfoResponse;
import vook.server.api.web.routes.user.reqres.UserOnboardingCompleteRequest;
import vook.server.api.web.routes.user.reqres.UserRegisterRequest;

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
    @PostMapping("/onboarding/complete")
    public CommonApiResponse<Void> onboardingComplete(
            @AuthenticationPrincipal VookLoginUser user,
            @RequestBody UserOnboardingCompleteRequest request
    ) {
        service.onboardingComplete(user, request);
        return CommonApiResponse.ok();
    }
}
