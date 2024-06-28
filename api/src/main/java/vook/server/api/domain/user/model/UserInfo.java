package vook.server.api.domain.user.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_info")
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

    public static UserInfo forRegisterOf(
            String nickname,
            User user,
            Boolean marketingEmailOptIn
    ) {
        UserInfo result = new UserInfo();
        result.nickname = nickname;
        result.user = user;
        result.marketingEmailOptIn = marketingEmailOptIn;
        return result;
    }

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
