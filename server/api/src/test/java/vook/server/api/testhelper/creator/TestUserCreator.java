package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.user.logic.UserOnboardingCommand;
import vook.server.api.domain.user.logic.UserRegisterCommand;
import vook.server.api.domain.user.logic.UserSignUpFromSocialCommand;
import vook.server.api.domain.user.model.social_user.SocialUser;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.domain.user.model.user_info.Funnel;
import vook.server.api.domain.user.model.user_info.Job;
import vook.server.api.web.common.auth.app.TokenService;
import vook.server.api.web.common.auth.data.GeneratedToken;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
@RequiredArgsConstructor
public class TestUserCreator {

    private final UserLogic userLogic;
    private final TokenService tokenService;
    private final AtomicInteger userCounter = new AtomicInteger(0);

    public User createUnregisteredUser() {
        SocialUser user = userLogic.signUpFromSocial(
                UserSignUpFromSocialCommand.builder()
                        .provider("testProvider")
                        .providerUserId("testProviderUserId" + userCounter.getAndIncrement())
                        .email("testEmail" + userCounter.getAndIncrement() + "@test.com")
                        .build()
        );
        return user.getUser();
    }

    public User createRegisteredUser() {
        User user = createUnregisteredUser();
        userLogic.register(
                UserRegisterCommand.builder()
                        .userUid(user.getUid())
                        .nickname("testNick")
                        .marketingEmailOptIn(true)
                        .build()
        );
        return userLogic.getByUid(user.getUid());
    }

    public User createCompletedOnboardingUser() {
        User user = createRegisteredUser();
        userLogic.onboarding(
                UserOnboardingCommand.builder()
                        .userUid(user.getUid())
                        .funnel(Funnel.OTHER)
                        .job(Job.OTHER)
                        .build()
        );
        return userLogic.getByUid(user.getUid());
    }

    public User createWithdrawnUser() {
        User user = createCompletedOnboardingUser();
        userLogic.withdraw(user.getUid());
        return userLogic.getByUid(user.getUid());
    }

    public GeneratedToken createToken(User user) {
        return tokenService.generateToken(user.getUid());
    }
}
