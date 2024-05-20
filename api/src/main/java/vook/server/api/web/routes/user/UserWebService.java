package vook.server.api.web.routes.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.terms.TermsService;
import vook.server.api.app.user.UserService;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.model.terms.Terms;
import vook.server.api.model.user.User;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWebService {

    private final UserService userService;
    private final TermsService termsService;

    public UserInfoResponse userInfo(VookLoginUser loginUser) {
        User user = userService.findByUid(loginUser.getUid()).orElseThrow();
        return UserInfoResponse.from(user);
    }

    public List<UserTermsResponse> terms() {
        List<Terms> terms = termsService.findAll();
        return UserTermsResponse.from(terms);
    }

    public void register(VookLoginUser loginUser, UserRegisterRequest request) {
        List<Long> agreeTermsId = request.getAgreeTermsId();
        if (!termsService.includeAllRequiredTerms(agreeTermsId)) {
            throw new IllegalArgumentException("동의 필수인 약관이 누락되었습니다.");
        }
        userService.register(request.toCommand(loginUser.getUid(), termsService::find));
    }

    public void onboardingComplete(VookLoginUser loginUser, UserOnboardingCompleteRequest request) {
        userService.completeOnboarding(request.toCommand(loginUser.getUid()));
    }
}
