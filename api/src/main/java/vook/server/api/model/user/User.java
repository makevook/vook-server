package vook.server.api.model.user;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SocialUser> socialUsers = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserInfo userInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserTermsAgree> userTermsAgrees = new ArrayList<>();

    public static User forSignUpFromSocialOf(
            String email
    ) {
        User user = new User();
        user.uid = UUID.randomUUID().toString();
        user.email = email;
        user.status = UserStatus.SOCIAL_LOGIN_COMPLETED;
        return user;
    }

    public void addSocialUser(SocialUser socialUser) {
        socialUsers.add(socialUser);
    }

    public void addUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void addUserTermsAgree(UserTermsAgree userTermsAgree) {
        userTermsAgrees.add(userTermsAgree);
    }
}
