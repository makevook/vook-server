package vook.server.api.app.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import vook.server.api.model.user.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
