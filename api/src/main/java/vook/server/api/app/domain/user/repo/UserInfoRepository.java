package vook.server.api.app.domain.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
