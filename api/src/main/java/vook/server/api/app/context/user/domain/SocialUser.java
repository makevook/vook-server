package vook.server.api.app.context.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "social_user")
@EntityListeners(AuditingEntityListener.class)
public class SocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;

    private String providerUserId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

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
