package vook.server.api.domain.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import vook.server.api.domain.user.exception.*;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<SocialUser> socialUsers = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserInfo userInfo;

    public static User forSignUpFromSocialOf(
            String email
    ) {
        User user = new User();
        user.uid = UUID.randomUUID().toString();
        user.email = email;
        user.status = UserStatus.SOCIAL_LOGIN_COMPLETED;
        user.onboardingCompleted = false;
        return user;
    }

    public void addSocialUser(SocialUser socialUser) {
        socialUsers.add(socialUser);
    }

    public void register(UserInfo userInfo) {
        this.status = UserStatus.REGISTERED;
        this.userInfo = userInfo;
        this.registeredAt = LocalDateTime.now();
    }

    public void onboarding(Funnel funnel, Job job) {
        this.onboardingCompleted = true;
        this.userInfo.addOnboardingInfo(funnel, job);
        this.onboardingCompletedAt = LocalDateTime.now();
    }

    public void update(String nickname) {
        this.userInfo.update(nickname);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void withdraw() {
        this.status = UserStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
    }

    public void reRegister(String nickname, Boolean marketingEmailOptIn) {
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
