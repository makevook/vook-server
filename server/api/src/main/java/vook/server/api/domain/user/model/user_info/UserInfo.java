package vook.server.api.domain.user.model.user_info;

import jakarta.persistence.*;
import lombok.*;
import vook.server.api.domain.user.model.user.User;

@Getter
@Entity
@Table(name = "user_info")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String nickname;

    private Boolean marketingEmailOptIn;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private Funnel funnel;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private Job job;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addOnboardingInfo(Funnel funnel, Job job) {
        this.funnel = funnel;
        this.job = job;
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void reRegister(String nickname, Boolean marketingEmailOptIn) {
        this.nickname = nickname;
        this.marketingEmailOptIn = marketingEmailOptIn;

        funnel = null;
        job = null;
    }
}
