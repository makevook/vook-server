package vook.server.api.domain.user.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.exception.*;
import vook.server.api.domain.user.model.*;
import vook.server.api.domain.user.service.data.OnboardingCommand;
import vook.server.api.domain.user.service.data.RegisterCommand;
import vook.server.api.domain.user.service.data.SignUpFromSocialCommand;
import vook.server.api.globalcommon.annotation.DomainService;

import java.util.Optional;

@DomainService
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
                .findByEmail(command.email())
                .orElseGet(() -> repository.save(command.toNewUser()));

        SocialUser savedSocialUser = socialUserRepository.save(command.toSocialUser(user));
        user.addSocialUser(savedSocialUser);

        return savedSocialUser;
    }

    public User getByUid(@NotBlank String uid) {
        return getUserByUid(uid);
    }

    public void register(@Valid RegisterCommand command) {
        User user = getUserByUid(command.userUid());
        if (user.isRegistered()) {
            throw new AlreadyRegisteredException();
        }
        if (user.isWithdrawn()) {
            throw new WithdrawnUserException();
        }

        UserInfo userInfo = userInfoRepository.save(UserInfo.forRegisterOf(
                command.nickname(),
                user,
                command.marketingEmailOptIn()
        ));
        user.register(userInfo);
    }

    public void onboarding(@Valid OnboardingCommand command) {
        User user = getUserByUid(command.userUid());
        if (!user.isReadyToOnboarding()) {
            throw new NotReadyToOnboardingException();
        }
        if (user.getOnboardingCompleted()) {
            throw new AlreadyOnboardingException();
        }

        user.onboarding(command.funnel(), command.job());
    }

    public void updateInfo(
            @NotBlank String uid,
            @NotBlank @Size(min = 1, max = 10) String nickname
    ) {
        User user = getUserByUid(uid);
        if (!user.isRegistered()) {
            throw new NotRegisteredException();
        }
        user.update(nickname);
    }

    public void withdraw(@NotBlank String uid) {
        User user = getUserByUid(uid);
        if (user.isWithdrawn()) {
            return;
        }
        user.withdraw();
    }

    private User getUserByUid(String uid) {
        return repository.findByUid(uid).orElseThrow(UserNotFoundException::new);
    }
}
