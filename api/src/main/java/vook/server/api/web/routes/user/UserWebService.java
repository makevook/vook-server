package vook.server.api.web.routes.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.user.UserService;
import vook.server.api.model.user.User;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.user.reqres.UserInfoResponse;
import vook.server.api.web.routes.user.reqres.UserOnboardingRequest;
import vook.server.api.web.routes.user.reqres.UserRegisterRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWebService {

    private final UserService userService;

    public UserInfoResponse userInfo(VookLoginUser loginUser) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        return UserInfoResponse.from(user);
    }

    public void register(VookLoginUser loginUser, UserRegisterRequest request) {
        userService.register(request.toCommand(loginUser.getUid()));
    }

    public void onboarding(VookLoginUser loginUser, UserOnboardingRequest request) {
        userService.onboarding(request.toCommand(loginUser.getUid()));
    }
}
