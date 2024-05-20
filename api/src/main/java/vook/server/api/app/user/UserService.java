package vook.server.api.app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.model.user.SocialUser;
import vook.server.api.model.user.User;
import vook.server.api.model.user.UserInfo;
import vook.server.api.model.user.UserTermsAgree;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final SocialUserRepository socialUserRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserTermsAgreeRepository userTermsAgreeRepository;

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

        UserInfo userInfo = UserInfo.forRegisterOf(command.getNickname(), user);
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        user.addUserInfo(savedUserInfo);

        for (RegisterCommand.TermsAgree termsAgree : command.getTermsAgrees()) {
            UserTermsAgree userTermsAgree = UserTermsAgree.of(user, termsAgree.getTerms(), termsAgree.getAgree());
            UserTermsAgree savedUserTermsAgree = userTermsAgreeRepository.save(userTermsAgree);
            user.addUserTermsAgree(savedUserTermsAgree);
        }
    }
}
