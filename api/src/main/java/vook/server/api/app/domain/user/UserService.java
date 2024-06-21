package vook.server.api.app.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.app.domain.user.data.OnboardingCommand;
import vook.server.api.app.domain.user.data.RegisterCommand;
import vook.server.api.app.domain.user.data.SignUpFromSocialCommand;
import vook.server.api.app.domain.user.exception.*;
import vook.server.api.app.domain.user.model.SocialUserRepository;
import vook.server.api.app.domain.user.model.UserInfoRepository;
import vook.server.api.app.domain.user.model.UserRepository;
import vook.server.api.app.domain.user.model.SocialUser;
import vook.server.api.app.domain.user.model.User;
import vook.server.api.app.domain.user.model.UserInfo;

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
        if (user.isRegistered()) {
            throw new AlreadyRegisteredException();
        }
        if (user.isWithdrawn()) {
            throw new WithdrawnUserException();
        }

        UserInfo userInfo = userInfoRepository.save(UserInfo.forRegisterOf(
                command.getNickname(),
                user,
                command.getMarketingEmailOptIn()
        ));
        user.register(userInfo);
    }

    public void onboarding(OnboardingCommand command) {
        User user = repository.findByUid(command.getUserUid()).orElseThrow();
        if (!user.isReadyToOnboarding()) {
            throw new NotReadyToOnboardingException();
        }
        if (user.getOnboardingCompleted()) {
            throw new AlreadyOnboardingException();
        }

        user.onboarding(command.getFunnel(), command.getJob());
    }

    public void updateInfo(String uid, String nickname) {
        User user = repository.findByUid(uid).orElseThrow();
        if (!user.isRegistered()) {
            throw new NotRegisteredException();
        }
        user.update(nickname);
    }

    public void withdraw(String uid) {
        User user = repository.findByUid(uid).orElseThrow();
        if (user.isWithdrawn()) {
            return;
        }
        user.withdraw();
    }
}
