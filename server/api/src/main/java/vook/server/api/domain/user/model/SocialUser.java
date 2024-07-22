package vook.server.api.domain.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "social_user")
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
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
}
