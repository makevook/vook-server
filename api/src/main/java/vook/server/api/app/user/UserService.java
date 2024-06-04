package vook.server.api.app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.user.data.CompleteOnboardingCommand;
import vook.server.api.app.user.data.RegisterCommand;
import vook.server.api.app.user.data.SignUpFromSocialCommand;
import vook.server.api.app.user.repo.SocialUserRepository;
import vook.server.api.app.user.repo.UserInfoRepository;
import vook.server.api.app.user.repo.UserRepository;
import vook.server.api.model.user.SocialUser;
import vook.server.api.model.user.User;
import vook.server.api.model.user.UserInfo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final SocialUserRepository socialUserRepository;
    private final UserInfoRepository userInfoRepository;

    public Optional<SocialUser> findByProvider(String provider, String providerUserId) {
        return socialUserRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }

    public SocialUser signUpFromSocial(SignUpFromSocialCommand command) {
        User user = repository
                .findByEmail(command.getEmail())
                .orElseGet(() -> repository.save(command.toNewUser()));

        SocialUser savedSocialUser = socialUserRepository.save(command.toSocialUser(user));
        user.addSocialUser(savedSocialUser);

        return savedSocialUser;
    }

    public Optional<User> findByUid(String uid) {
        return repository.findByUid(uid);
    }

    public void register(RegisterCommand command) {
        User user = repository.findByUid(command.getUserUid()).orElseThrow();

        UserInfo userInfo = UserInfo.forRegisterOf(
                command.getNickname(),
                user,
                command.isMarketingEmailOptIn()
        );
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        user.addUserInfo(savedUserInfo);

        user.registered();
    }

    public void completeOnboarding(CompleteOnboardingCommand command) {
        User user = repository.findByUid(command.getUserUid()).orElseThrow();
        user.onboardingCompleted();

        user.getUserInfo().addOnboardingInfo(command.getFunnel(), command.getJob());
    }
}
