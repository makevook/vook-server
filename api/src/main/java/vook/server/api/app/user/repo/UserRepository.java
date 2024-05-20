package vook.server.api.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUid(String uid);
}
