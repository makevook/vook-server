package vook.server.api.app.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vook.server.api.model.user.SocialUser;
import vook.server.api.model.user.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final SocialUserRepository socialUserRepository;

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
}
