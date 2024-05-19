package vook.server.api.model.user;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "social_user")
public class SocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    private String providerUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static SocialUser forNewOf(
            String provider,
            String providerUserId,
            User user
    ) {
        SocialUser socialUser = new SocialUser();
        socialUser.provider = provider;
        socialUser.providerUserId = providerUserId;
        socialUser.user = user;
        return socialUser;
    }
}
