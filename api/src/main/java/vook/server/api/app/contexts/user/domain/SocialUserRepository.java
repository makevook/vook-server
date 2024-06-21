package vook.server.api.app.contexts.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
    Optional<SocialUser> findByProviderAndProviderUserId(String provider, String providerUserId);
}
