package vook.server.api.web.routes.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;

import java.util.List;

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
    @GetMapping("/terms")
    public CommonApiResponse<List<UserTermsResponse>> terms() {
        return CommonApiResponse.okWithResult(service.terms());
    }
}
