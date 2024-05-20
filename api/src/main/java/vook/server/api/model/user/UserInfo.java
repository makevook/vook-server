package vook.server.api.model.user;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static UserInfo forRegisterOf(
            String nickname,
            User user
    ) {
        UserInfo result = new UserInfo();
        result.nickname = nickname;
        return result;
    }
}
