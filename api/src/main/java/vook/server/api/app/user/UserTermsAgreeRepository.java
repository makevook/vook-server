package vook.server.api.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.UserTermsAgree;

public interface UserTermsAgreeRepository extends JpaRepository<UserTermsAgree, Long> {
}
