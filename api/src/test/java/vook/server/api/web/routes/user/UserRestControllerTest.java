package vook.server.api.web.routes.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import vook.server.api.model.user.User;
import vook.server.api.testhelper.HttpEntityBuilder;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.TestDataCreator;
import vook.server.api.web.auth.data.GeneratedToken;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserRestControllerTest extends IntegrationTestBase {

    @MockBean
    UserWebService webService;

    @Autowired
    TestDataCreator testDataCreator;

    @Test
    void userInfo() {
        // given
        User unregisteredUser = testDataCreator.createUnregisteredUser();
        GeneratedToken token = testDataCreator.createToken(unregisteredUser);

        // when
        var res = rest.exchange(
                "/user/info",
                HttpMethod.GET,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .build(),
                UserApi.UserApiUerInfoResponse.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원 가입 - 정상")
    void register() {
        // given
        User unregisteredUser = testDataCreator.createUnregisteredUser();
        GeneratedToken token = testDataCreator.createToken(unregisteredUser);

        // when
        var res = rest.exchange(
                "/user/register",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of(
                                "nickname", "testNickname",
                                "onboardingComplete", true
                        ))
                        .build(),
                String.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원 가입 - 닉네임 누락")
    void registerWithoutNickname() {
        // given
        User unregisteredUser = testDataCreator.createUnregisteredUser();
        GeneratedToken token = testDataCreator.createToken(unregisteredUser);

        // when
        var res = rest.exchange(
                "/user/register",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of(
                                "onboardingComplete", true
                        ))
                        .build(),
                String.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
