package vook.server.api.domain.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.exception.*;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.model.UserStatus;
import vook.server.api.domain.user.service.data.OnboardingCommand;
import vook.server.api.domain.user.service.data.RegisterCommand;
import vook.server.api.domain.user.service.data.SignUpFromSocialCommand;
import vook.server.api.globalcommon.exception.ParameterValidateException;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class UserServiceTest extends IntegrationTestBase {

    @Autowired
    UserService service;

    @Autowired
    TestUserCreator testUserCreator;

    @TestFactory
    @DisplayName("사용자 정보 조회(provider) - 실패; 파라미터 룰 위반")
    Collection<DynamicTest> findByProvider_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("provider가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.findByProvider(null, "providerUserId"))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("provider가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.findByProvider("", "providerUserId"))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("providerUserId가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.findByProvider("provider", null))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("providerUserId가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.findByProvider("provider", ""))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
    }

    @TestFactory
    @DisplayName("소셜에서 회원가입 - 실패; 파라미터 룰 위반")
    Collection<DynamicTest> signUpFromSocial_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("provider가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of(null, "providerUserId", "email")))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("provider가 빈 문자인 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of("", "providerUserId", "email")))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("providerUserId가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of("provider", null, "email")))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("providerUserId가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of("provider", "", "email")))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("email이 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of("provider", "providerUserId", null)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("email이 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of("provider", "providerUserId", "")))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("email이 이메일 형식이 아닌 경우", () -> {
                    assertThatThrownBy(() -> service.signUpFromSocial(SignUpFromSocialCommand.of("provider", "providerUserId", "email")))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
    }

    @Test
    @DisplayName("사용자 정보 조회(uid) - 정상; 회원가입 전 사용자")
    void getByUid1() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();

        // when
        var user = service.getByUid(unregisteredUser.getUid());

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUid()).isEqualTo(unregisteredUser.getUid());
        assertThat(user.getEmail()).isEqualTo(unregisteredUser.getEmail());
        assertThat(user.getUserInfo()).isNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.SOCIAL_LOGIN_COMPLETED);
        assertThat(user.getOnboardingCompleted()).isFalse();
    }

    @Test
    @DisplayName("사용자 정보 조회(uid) - 정상; 회원가입 후 사용자")
    void getByUid2() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        // when
        var user = service.getByUid(registeredUser.getUid());

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUid()).isEqualTo(registeredUser.getUid());
        assertThat(user.getEmail()).isEqualTo(registeredUser.getEmail());
        assertThat(user.getUserInfo().getNickname()).isEqualTo(registeredUser.getUserInfo().getNickname());
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getOnboardingCompleted()).isFalse();
    }

    @Test
    @DisplayName("사용자 정보 조회(uid) - 정상; 온보딩 완료 사용자")
    void getByUid3() {
        // given
        User completedOnboardingUser = testUserCreator.createCompletedOnboardingUser();

        // when
        var user = service.getByUid(completedOnboardingUser.getUid());

        // then
        assertThat(user).isNotNull();
        assertThat(user.getUid()).isEqualTo(completedOnboardingUser.getUid());
        assertThat(user.getEmail()).isEqualTo(completedOnboardingUser.getEmail());
        assertThat(user.getUserInfo().getNickname()).isEqualTo(completedOnboardingUser.getUserInfo().getNickname());
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getOnboardingCompleted()).isTrue();
    }

    @TestFactory
    @DisplayName("사용자 정보 조회(uid) - 실패; 파라미터 룰 위반")
    Collection<DynamicTest> getByUid_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("유저 UID가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.getByUid(null))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("유저 UID가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.getByUid(""))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
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
        User user = service.getByUid(unregisteredUser.getUid());
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

    @TestFactory
    @DisplayName("회원 가입 - 에러; 파라미터 룰 위반")
    Collection<DynamicTest> register_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("유저 UID가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.register(RegisterCommand.of(null, "nickname", true)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("유저 UID가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.register(RegisterCommand.of("", "nickname", true)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("닉네임이 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.register(RegisterCommand.of("uid", null, true)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("닉네임이 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.register(RegisterCommand.of("uid", "", true)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("닉네임 길이가 제한을 넘긴 경우", () -> {
                    assertThatThrownBy(() -> service.register(RegisterCommand.of("uid", "12345678901", true)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("마케팅 이메일 수신 동의가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.register(RegisterCommand.of("uid", "nickname", null)))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
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
        User user = service.getByUid(registeredUser.getUid());
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

    @TestFactory
    @DisplayName("온보딩 완료 - 에러; 파라미터 룰 위반")
    Collection<DynamicTest> onboarding_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("유저 UID가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.onboarding(OnboardingCommand.of(null, Funnel.OTHER, Job.OTHER)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("유저 UID가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.onboarding(OnboardingCommand.of("", Funnel.OTHER, Job.OTHER)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("퍼널이 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.onboarding(OnboardingCommand.of("uid", null, Job.OTHER)))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("직업이 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.onboarding(OnboardingCommand.of("uid", Funnel.OTHER, null)))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
    }

    @Test
    @DisplayName("사용자 정보 수정 - 정상")
    void updateInfo1() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        // when
        service.updateInfo(registeredUser.getUid(), "newNick");

        // then
        User user = service.getByUid(registeredUser.getUid());
        assertThat(user.getUserInfo().getNickname()).isEqualTo("newNick");
        assertThat(user.getLastUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("사용자 정보 수정 - 에러; 미 가입 유저")
    void updateInfoError1() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();

        // when
        assertThatThrownBy(() -> service.updateInfo(unregisteredUser.getUid(), "newNick"))
                .isInstanceOf(NotRegisteredException.class);
    }

    @TestFactory
    @DisplayName("사용자 정보 수정 - 에러; 파라미터 룰 위반")
    Collection<DynamicTest> updateInfo_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("유저 UID가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.updateInfo(null, "newNickname"))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("유저 UID가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.updateInfo("", "newNickname"))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("닉네임이 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.updateInfo("uid", null))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("닉네임이 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.updateInfo("uid", ""))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("닉네임 길이가 제한을 넘긴 경우", () -> {
                    assertThatThrownBy(() -> service.updateInfo("uid", "12345678901"))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
    }

    @Test
    @DisplayName("탈퇴 - 정상")
    void withdraw1() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();

        // when
        service.withdraw(registeredUser.getUid());

        // then
        User user = service.getByUid(registeredUser.getUid());
        assertThat(user.getStatus()).isEqualTo(UserStatus.WITHDRAWN);
        assertThat(user.getWithdrawnAt()).isNotNull();
    }

    @TestFactory
    @DisplayName("탈퇴 - 에러; 파라미터 룰 위반")
    Collection<DynamicTest> withdraw_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("유저 UID가 누락된 경우", () -> {
                    assertThatThrownBy(() -> service.withdraw(null))
                            .isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("유저 UID가 빈 문자열인 경우", () -> {
                    assertThatThrownBy(() -> service.withdraw(""))
                            .isInstanceOf(ParameterValidateException.class);
                })
        );
    }
}
