package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.application.data.OnboardingCommand;
import vook.server.api.app.contexts.user.application.data.RegisterCommand;
import vook.server.api.app.contexts.user.application.data.SignUpFromSocialCommand;
import vook.server.api.app.contexts.user.domain.Funnel;
import vook.server.api.app.contexts.user.domain.Job;
import vook.server.api.app.contexts.user.domain.SocialUser;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.web.auth.app.TokenService;
import vook.server.api.web.auth.data.GeneratedToken;

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
                SignUpFromSocialCommand.of("testProvider", "testProviderUserId" + userCounter.getAndIncrement(), "testEmail" + userCounter.getAndIncrement() + "@test.com")
        );
        return user.getUser();
    }

    public User createRegisteredUser() {
        User user = createUnregisteredUser();
        userService.register(RegisterCommand.of(user.getUid(), "testNick", true));
        return userService.findByUid(user.getUid()).orElseThrow();
    }

    public User createCompletedOnboardingUser() {
        User user = createRegisteredUser();
        userService.onboarding(OnboardingCommand.of(user.getUid(), Funnel.OTHER, Job.OTHER));
        return userService.findByUid(user.getUid()).orElseThrow();
    }

    public User createWithdrawnUser() {
        User user = createCompletedOnboardingUser();
        userService.withdraw(user.getUid());
        return userService.findByUid(user.getUid()).orElseThrow();
    }

    public GeneratedToken createToken(User user) {
        return tokenService.generateToken(user.getUid());
    }
}
