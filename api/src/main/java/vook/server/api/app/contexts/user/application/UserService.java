package vook.server.api.app.contexts.user.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import vook.server.api.app.contexts.user.application.data.OnboardingCommand;
import vook.server.api.app.contexts.user.application.data.RegisterCommand;
import vook.server.api.app.contexts.user.application.data.SignUpFromSocialCommand;
import vook.server.api.app.contexts.user.domain.*;
import vook.server.api.app.contexts.user.exception.*;

import java.util.Optional;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final SocialUserRepository socialUserRepository;
    private final UserInfoRepository userInfoRepository;

    public Optional<SocialUser> findByProvider(@NotBlank String provider, @NotBlank String providerUserId) {
        return socialUserRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }

    public SocialUser signUpFromSocial(@Valid SignUpFromSocialCommand command) {
        User user = repository
                .findByEmail(command.getEmail())
                .orElseGet(() -> repository.save(command.toNewUser()));

        SocialUser savedSocialUser = socialUserRepository.save(command.toSocialUser(user));
        user.addSocialUser(savedSocialUser);

        return savedSocialUser;
    }

    public User getByUid(@NotBlank String uid) {
        return repository.findByUid(uid).orElseThrow(UserNotFoundException::new);
    }

    public void register(@Valid RegisterCommand command) {
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

    public void onboarding(@Valid OnboardingCommand command) {
        User user = repository.findByUid(command.getUserUid()).orElseThrow();
        if (!user.isReadyToOnboarding()) {
            throw new NotReadyToOnboardingException();
        }
        if (user.getOnboardingCompleted()) {
            throw new AlreadyOnboardingException();
        }

        user.onboarding(command.getFunnel(), command.getJob());
    }

    public void updateInfo(
            @NotBlank String uid,
            @NotBlank @Size(min = 1, max = 10) String nickname
    ) {
        User user = repository.findByUid(uid).orElseThrow();
        if (!user.isRegistered()) {
            throw new NotRegisteredException();
        }
        user.update(nickname);
    }

    public void withdraw(@NotBlank String uid) {
        User user = repository.findByUid(uid).orElseThrow();
        if (user.isWithdrawn()) {
            return;
        }
        user.withdraw();
    }
}
