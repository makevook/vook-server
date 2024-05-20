package vook.server.api.web.routes.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.user.UserService;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.model.user.User;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWebService {

    private final UserService service;

    public UserInfoResponse userInfo(VookLoginUser loginUser) {
        User user = service.findByUid(loginUser.getUid()).orElseThrow();
        return UserInfoResponse.from(user);
    }
}
