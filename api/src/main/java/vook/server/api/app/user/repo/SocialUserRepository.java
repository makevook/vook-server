package vook.server.api.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.SocialUser;

import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    Optional<SocialUser> findByProviderAndProviderUserId(String provider, String providerUserId);
}
