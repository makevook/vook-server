package vook.server.api.testhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.user.UserService;
import vook.server.api.app.user.data.CompleteOnboardingCommand;
import vook.server.api.app.user.data.RegisterCommand;
import vook.server.api.app.user.data.SignUpFromSocialCommand;
import vook.server.api.model.user.SocialUser;
import vook.server.api.model.user.User;
import vook.server.api.web.auth.app.TokenService;
import vook.server.api.web.auth.data.GeneratedToken;

@Component
@Transactional
@RequiredArgsConstructor
public class TestDataCreator {

    private final UserService userService;
    private final TokenService tokenService;

    public User createUnregisteredUser() {
        SocialUser user = userService.signUpFromSocial(
                SignUpFromSocialCommand.of("testProvider", "testProviderUserId", "testEmail@test.com")
        );
        return user.getUser();
    }

    public User createRegisteredUser() {
        User user = createUnregisteredUser();
        userService.register(RegisterCommand.of(user.getUid(), "testNickname", true));
        return userService.findByUid(user.getUid()).orElseThrow();
    }

    public User createCompletedOnboardingUser() {
        User user = createRegisteredUser();
        userService.completeOnboarding(CompleteOnboardingCommand.of(user.getUid(), "testFunnel", "testJob"));
        return userService.findByUid(user.getUid()).orElseThrow();
    }

    public GeneratedToken createToken(User user) {
        return tokenService.generateToken(user.getUid());
    }
}
