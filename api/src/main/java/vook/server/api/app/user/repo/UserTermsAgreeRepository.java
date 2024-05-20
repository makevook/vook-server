package vook.server.api.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.UserTermsAgree;

public interface UserTermsAgreeRepository extends JpaRepository<UserTermsAgree, Long> {
}
