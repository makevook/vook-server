package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.domain.user.model.SocialUser;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.user.service.data.UserOnboardingCommand;
import vook.server.api.domain.user.service.data.UserRegisterCommand;
import vook.server.api.domain.user.service.data.UserSignUpFromSocialCommand;
import vook.server.api.web.common.auth.app.TokenService;
import vook.server.api.web.common.auth.data.GeneratedToken;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
@RequiredArgsConstructor
public class TestUserCreator {

    private final UserService userService;
    private final TokenService tokenService;
    private final AtomicInteger userCounter = new AtomicInteger(0);

    public User createUnregisteredUser() {
        SocialUser user = userService.signUpFromSocial(
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
        userService.register(
                UserRegisterCommand.builder()
                        .userUid(user.getUid())
                        .nickname("testNick")
                        .marketingEmailOptIn(true)
                        .build()
        );
        return userService.getByUid(user.getUid());
    }

    public User createCompletedOnboardingUser() {
        User user = createRegisteredUser();
        userService.onboarding(
                UserOnboardingCommand.builder()
                        .userUid(user.getUid())
                        .funnel(Funnel.OTHER)
                        .job(Job.OTHER)
                        .build()
        );
        return userService.getByUid(user.getUid());
    }

    public User createWithdrawnUser() {
        User user = createCompletedOnboardingUser();
        userService.withdraw(user.getUid());
        return userService.getByUid(user.getUid());
    }

    public GeneratedToken createToken(User user) {
        return tokenService.generateToken(user.getUid());
    }
}
