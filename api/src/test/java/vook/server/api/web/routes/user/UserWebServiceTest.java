package vook.server.api.web.routes.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.user.UserService;
import vook.server.api.config.auth.common.VookLoginUser;
import vook.server.api.model.user.User;
import vook.server.api.model.user.UserStatus;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.TestDataCreator;
import vook.server.api.web.routes.user.reqres.UserInfoResponse;
import vook.server.api.web.routes.user.reqres.UserOnboardingCompleteRequest;
import vook.server.api.web.routes.user.reqres.UserRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserWebServiceTest extends IntegrationTestBase {

    @Autowired
    UserWebService userWebService;

    @Autowired
    TestDataCreator testDataCreator;
    @Autowired
    UserService userService;

    @Test
    @DisplayName("사용자 정보 조회 - 정상; 회원가입 전 사용자")
    void userInfo1() {
        // given
        User unregisteredUser = testDataCreator.createUnregisteredUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(unregisteredUser.getUid());

        // when
        UserInfoResponse response = userWebService.userInfo(vookLoginUser);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUid()).isEqualTo(unregisteredUser.getUid());
        assertThat(response.getEmail()).isEqualTo(unregisteredUser.getEmail());
        assertThat(response.getNickname()).isNull();
        assertThat(response.getStatus()).isEqualTo(UserStatus.SOCIAL_LOGIN_COMPLETED);
    }

    @Test
    @DisplayName("사용자 정보 조회 - 정상; 회원가입 후 사용자")
    void userInfo2() {
        // given
        User registeredUser = testDataCreator.createRegisteredUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(registeredUser.getUid());

        // when
        UserInfoResponse response = userWebService.userInfo(vookLoginUser);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUid()).isEqualTo(registeredUser.getUid());
        assertThat(response.getEmail()).isEqualTo(registeredUser.getEmail());
        assertThat(response.getNickname()).isEqualTo(registeredUser.getUserInfo().getNickname());
        assertThat(response.getStatus()).isEqualTo(UserStatus.REGISTERED);
    }

    @Test
    @DisplayName("사용자 정보 조회 - 정상; 온보딩 완료 사용자")
    void userInfo3() {
        // given
        User completedOnboardingUser = testDataCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(completedOnboardingUser.getUid());

        // when
        UserInfoResponse response = userWebService.userInfo(vookLoginUser);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUid()).isEqualTo(completedOnboardingUser.getUid());
        assertThat(response.getEmail()).isEqualTo(completedOnboardingUser.getEmail());
        assertThat(response.getNickname()).isEqualTo(completedOnboardingUser.getUserInfo().getNickname());
        assertThat(response.getStatus()).isEqualTo(UserStatus.ONBOARDING_COMPLETED);
    }

    @Test
    @DisplayName("회원 가입 - 정상")
    void register1() {
        // given
        User unregisteredUser = testDataCreator.createUnregisteredUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(unregisteredUser.getUid());

        UserRegisterRequest request = new UserRegisterRequest();
        request.setNickname("nickname");
        request.setRequiredTermsAgree(true);
        request.setMarketingEmailOptIn(true);

        // when
        userWebService.register(vookLoginUser, request);

        // then
        User user = userService.findByUid(unregisteredUser.getUid()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(UserStatus.REGISTERED);
        assertThat(user.getUserInfo()).isNotNull();
        assertThat(user.getUserInfo().getNickname()).isEqualTo(request.getNickname());
        assertThat(user.getUserInfo().getMarketingEmailOptIn()).isEqualTo(request.isMarketingEmailOptIn());
    }

    @Test
    @DisplayName("온보딩 완료 - 정상")
    void onboardingComplete1() {
        // given
        User registeredUser = testDataCreator.createRegisteredUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(registeredUser.getUid());

        UserOnboardingCompleteRequest request = new UserOnboardingCompleteRequest();
        request.setFunnel("testFunnel");
        request.setJob("testJob");

        // when
        userWebService.onboardingComplete(vookLoginUser, request);

        // then
        User user = userService.findByUid(registeredUser.getUid()).orElseThrow();
        assertThat(user.getStatus()).isEqualTo(UserStatus.ONBOARDING_COMPLETED);
        assertThat(user.getUserInfo()).isNotNull();
        assertThat(user.getUserInfo().getFunnel()).isEqualTo(request.getFunnel());
        assertThat(user.getUserInfo().getJob()).isEqualTo(request.getJob());
    }
}
