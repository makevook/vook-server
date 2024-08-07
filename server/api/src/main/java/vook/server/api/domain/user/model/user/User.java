package vook.server.api.domain.user.model.user;

import jakarta.persistence.*;
import lombok.*;
import vook.server.api.domain.user.exception.*;
import vook.server.api.domain.user.model.social_user.SocialUser;
import vook.server.api.domain.user.model.user_info.Funnel;
import vook.server.api.domain.user.model.user_info.Job;
import vook.server.api.domain.user.model.user_info.UserInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uid;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30)")
    private UserStatus status;

    @Column(nullable = false)
    private Boolean onboardingCompleted;

    private LocalDateTime registeredAt;

    private LocalDateTime onboardingCompletedAt;

    private LocalDateTime lastUpdatedAt;

    private LocalDateTime withdrawnAt;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SocialUser> socialUsers = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserInfo userInfo;

    public void addSocialUser(SocialUser socialUser) {
        socialUsers.add(socialUser);
    }

    public void register(UserInfo userInfo) {
        this.status = UserStatus.REGISTERED;
        this.userInfo = userInfo;
        this.registeredAt = LocalDateTime.now();
    }

    public void onboarding(Funnel funnel, Job job) {
        validateOnboardingProcessReady();

        this.onboardingCompleted = true;
        this.userInfo.addOnboardingInfo(funnel, job);
        this.onboardingCompletedAt = LocalDateTime.now();
    }

    public void update(String nickname) {
        validateRegisterProcessCompleted();

        this.userInfo.update(nickname);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void withdraw() {
        if (this.status == UserStatus.WITHDRAWN) {
            return;
        }

        this.status = UserStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
    }

    public void reRegister(String nickname, Boolean marketingEmailOptIn) {
        validateReRegisterProcessReady();

        this.status = UserStatus.REGISTERED;
        this.onboardingCompleted = false;
        this.userInfo.reRegister(nickname, marketingEmailOptIn);
        this.registeredAt = LocalDateTime.now();
        this.lastUpdatedAt = null;
        this.withdrawnAt = null;
    }

    public void validateRegisterProcessReady() {
        if (status == UserStatus.REGISTERED) {
            throw new AlreadyRegisteredException();
        }
        if (status == UserStatus.WITHDRAWN) {
            throw new WithdrawnUserException();
        }
    }

    public void validateOnboardingProcessReady() {
        if (status != UserStatus.REGISTERED) {
            throw new NotReadyToOnboardingException();
        }
        if (this.onboardingCompleted) {
            throw new AlreadyOnboardingException();
        }
    }

    public void validateRegisterProcessCompleted() {
        if (status != UserStatus.REGISTERED) {
            throw new NotRegisteredException();
        }
        if (!this.onboardingCompleted) {
            throw new NotOnboardingException();
        }
    }

    public void validateReRegisterProcessReady() {
        if (status != UserStatus.WITHDRAWN) {
            throw new NotWithdrawnUserException();
        }
    }
}
