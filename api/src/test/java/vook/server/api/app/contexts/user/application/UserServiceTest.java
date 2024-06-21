package vook.server.api.app.contexts.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.application.data.OnboardingCommand;
import vook.server.api.app.contexts.user.application.data.RegisterCommand;
import vook.server.api.app.contexts.user.domain.Funnel;
import vook.server.api.app.contexts.user.domain.Job;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.user.domain.UserStatus;
import vook.server.api.app.contexts.user.exception.*;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class UserServiceTest extends IntegrationTestBase {

    @Autowired
    UserService service;

    @Autowired
    TestUserCreator testUserCreator;

    @Test
    @DisplayName("사용자 정보 조회 - 정상; 회원가입 전 사용자")
    void findByUid1() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();

        // when
        var user = service.findByUid(unregisteredUser.getUid()).orElseThrow();

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUid()).isEqualTo(unregisteredUser.getUid());
        assertThat(user.getEmail()).isEqualTo(unregisteredUser.getEmail());
        assertThat(user.getUserInfo()).isNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.SOCIAL_LOGIN_COMPLETED);
        assertThat(user.getOnboardingCompleted()).isFalse();
    }

    @Test
    @DisplayName("사용자 정보 조회 - 정상; 회원가입 후 사용자")
    void findByUid2() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        // when
        var user = service.findByUid(registeredUser.getUid()).orElseThrow();

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUid()).isEqualTo(registeredUser.getUid());
        assertThat(user.getEmail()).isEqualTo(registeredUser.getEmail());
        assertThat(user.getUserInfo().getNickname()).isEqualTo(registeredUser.getUserInfo().getNickname());
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getOnboardingCompleted()).isFalse();
    }

    @Test
    @DisplayName("사용자 정보 조회 - 정상; 온보딩 완료 사용자")
    void findByUid3() {
        // given
        User completedOnboardingUser = testUserCreator.createCompletedOnboardingUser();

        // when
        var user = service.findByUid(completedOnboardingUser.getUid()).orElseThrow();

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUid()).isEqualTo(completedOnboardingUser.getUid());
        assertThat(user.getEmail()).isEqualTo(completedOnboardingUser.getEmail());
        assertThat(user.getUserInfo().getNickname()).isEqualTo(completedOnboardingUser.getUserInfo().getNickname());
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getOnboardingCompleted()).isTrue();
    }

    @Test
    @DisplayName("회원 가입 - 정상")
    void register1() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();

        RegisterCommand command = RegisterCommand.of(
                unregisteredUser.getUid(),
                "nickname",
                true
        );

        // when
        service.register(command);

        // then
        User user = service.findByUid(unregisteredUser.getUid()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getOnboardingCompleted()).isFalse();
        assertThat(user.getRegisteredAt()).isNotNull();
        assertThat(user.getUserInfo()).isNotNull();
        assertThat(user.getUserInfo().getNickname()).isEqualTo(command.getNickname());
        assertThat(user.getUserInfo().getMarketingEmailOptIn()).isEqualTo(command.getMarketingEmailOptIn());
    }

    @Test
    @DisplayName("회원 가입 - 에러; 이미 가입된 유저")
    void registerError1() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        RegisterCommand command = RegisterCommand.of(
                registeredUser.getUid(),
                "nickname",
                true
        );

        // when
        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(AlreadyRegisteredException.class);
    }

    @Test
    @DisplayName("회원 가입 - 에러; 탈퇴한 유저")
    void registerError2() {
        // given
        User withdrawnUser = testUserCreator.createWithdrawnUser();

        var command = RegisterCommand.of(
                withdrawnUser.getUid(),
                "nickname",
                true
        );

        // when
        assertThatThrownBy(() -> service.register(command))
                .isInstanceOf(WithdrawnUserException.class);
    }

    @Test
    @DisplayName("온보딩 완료 - 정상")
    void onboarding1() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        var command = OnboardingCommand.of(
                registeredUser.getUid(),
                Funnel.OTHER,
                Job.OTHER
        );

        // when
        service.onboarding(command);

        // then
        User user = service.findByUid(registeredUser.getUid()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getOnboardingCompleted()).isTrue();
        assertThat(user.getOnboardingCompletedAt()).isNotNull();
        assertThat(user.getUserInfo()).isNotNull();
        assertThat(user.getUserInfo().getFunnel()).isEqualTo(command.getFunnel());
        assertThat(user.getUserInfo().getJob()).isEqualTo(command.getJob());
    }

    @Test
    @DisplayName("온보딩 완료 - 에러; 미 가입 유저")
    void onboardingError1() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();

        var command = OnboardingCommand.of(
                unregisteredUser.getUid(),
                Funnel.OTHER,
                Job.OTHER
        );

        // when
        assertThatThrownBy(() -> service.onboarding(command))
                .isInstanceOf(NotReadyToOnboardingException.class);
    }

    @Test
    @DisplayName("온보딩 완료 - 에러; 이미 온보딩 완료된 유저")
    void onboardingError2() {
        // given
        User completedOnboardingUser = testUserCreator.createCompletedOnboardingUser();

        var command = OnboardingCommand.of(
                completedOnboardingUser.getUid(),
                Funnel.OTHER,
                Job.OTHER
        );

        // when
        assertThatThrownBy(() -> service.onboarding(command))
                .isInstanceOf(AlreadyOnboardingException.class);
    }

    @Test
    @DisplayName("사용자 정보 수정 - 정상")
    void updateInfo1() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        // when
        service.updateInfo(registeredUser.getUid(), "newNickname");

        // then
        User user = service.findByUid(registeredUser.getUid()).orElseThrow();
        assertThat(user.getUserInfo().getNickname()).isEqualTo("newNickname");
        assertThat(user.getLastUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("사용자 정보 수정 - 에러; 미 가입 유저")
    void updateInfoError1() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();

        // when
        assertThatThrownBy(() -> service.updateInfo(unregisteredUser.getUid(), "newNickname"))
                .isInstanceOf(NotRegisteredException.class);
    }

    @Test
    @DisplayName("탈퇴 - 정상")
    void withdraw1() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        // when
        service.withdraw(registeredUser.getUid());

        // then
        User user = service.findByUid(registeredUser.getUid()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(UserStatus.WITHDRAWN);
        assertThat(user.getWithdrawnAt()).isNotNull();
    }
}
