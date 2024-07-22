package vook.server.api.domain.user.logic;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.user.exception.UserNotFoundException;
import vook.server.api.domain.user.model.social_user.SocialUser;
import vook.server.api.domain.user.model.social_user.SocialUserFactory;
import vook.server.api.domain.user.model.social_user.SocialUserRepository;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.domain.user.model.user.UserFactory;
import vook.server.api.domain.user.model.user.UserRepository;
import vook.server.api.domain.user.model.user_info.UserInfo;
import vook.server.api.domain.user.model.user_info.UserInfoFactory;
import vook.server.api.domain.user.model.user_info.UserInfoRepository;
import vook.server.api.globalcommon.annotation.DomainLogic;

import java.util.Optional;

@DomainLogic
@RequiredArgsConstructor
public class UserLogic {

    private final UserFactory userFactory;
    private final SocialUserFactory socialUserFactory;
    private final UserInfoFactory userInfoFactory;
    private final UserRepository repository;
    private final SocialUserRepository socialUserRepository;
    private final UserInfoRepository userInfoRepository;

    public Optional<SocialUser> findByProvider(@NotBlank String provider, @NotBlank String providerUserId) {
        return socialUserRepository.findByProviderAndProviderUserId(provider, providerUserId);
    }

    public SocialUser signUpFromSocial(@NotNull @Valid UserSignUpFromSocialCommand command) {
        User user = repository
                .findByEmail(command.email())
                .orElseGet(() -> repository.save(command.toNewUser(userFactory)));

        return socialUserRepository.save(command.toSocialUser(socialUserFactory, user));
    }

    public User getByUid(@NotBlank String uid) {
        return getUserByUid(uid);
    }

    public void register(@NotNull @Valid UserRegisterCommand command) {
        User user = getUserByUid(command.userUid());
        UserInfo userInfo = userInfoFactory.createForRegisterOf(
                command.nickname(),
                command.marketingEmailOptIn(),
                user
        );
        userInfoRepository.save(userInfo);
    }

    public void onboarding(@NotNull @Valid UserOnboardingCommand command) {
        User user = getUserByUid(command.userUid());
        user.onboarding(command.funnel(), command.job());
    }

    public void updateInfo(
            @NotBlank String uid,
            @NotBlank @Size(min = 1, max = 10) String nickname
    ) {
        User user = getUserByUid(uid);
        user.update(nickname);
    }

    public void withdraw(@NotBlank String uid) {
        User user = getUserByUid(uid);
        user.withdraw();
    }

    public void reRegister(@NotNull @Valid UserRegisterCommand command) {
        User user = getUserByUid(command.userUid());
        user.reRegister(command.nickname(), command.marketingEmailOptIn());
    }

    public void validateCompletedUserByUid(@NotBlank String uid) {
        User user = getUserByUid(uid);
        user.validateRegisterProcessCompleted();
    }

    private User getUserByUid(String uid) {
        return repository.findByUid(uid).orElseThrow(UserNotFoundException::new);
    }
}
