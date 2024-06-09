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

    private Boolean marketingEmailOptIn;

    @Enumerated(EnumType.STRING)
    private Funnel funnel;

    @Enumerated(EnumType.STRING)
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
}
