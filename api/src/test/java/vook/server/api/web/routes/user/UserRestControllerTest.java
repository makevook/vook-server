package vook.server.api.web.routes.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.domain.user.model.User;
import vook.server.api.testhelper.HttpEntityBuilder;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.web.auth.data.GeneratedToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class UserRestControllerTest extends IntegrationTestBase {

    @MockBean
    UserWebService webService;

    @Autowired
    TestUserCreator testUserCreator;

    @Test
    void userInfo() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();
        GeneratedToken token = testUserCreator.createToken(unregisteredUser);

        // when
        var res = rest.exchange(
                "/user/info",
                HttpMethod.GET,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .build(),
                UserApi.UserApiUserInfoResponse.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("회원 가입 - 정상")
    void register() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();
        GeneratedToken token = testUserCreator.createToken(unregisteredUser);

        // when
        var res = rest.exchange(
                "/user/register",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of(
                                "nickname", "testName",
                                "requiredTermsAgree", true,
                                "marketingEmailOptIn", true
                        ))
                        .build(),
                String.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @TestFactory
    @DisplayName("회원 가입 - 실패")
    Collection<DynamicTest> registerError() {
        // given
        User unregisteredUser = testUserCreator.createUnregisteredUser();
        GeneratedToken token = testUserCreator.createToken(unregisteredUser);

        Function<Map<String, Object>, ResponseEntity<String>> restExchange = body -> rest.exchange(
                "/user/register",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(body)
                        .build(),
                String.class
        );

        return List.of(
                DynamicTest.dynamicTest("닉네임 누락", () -> {
                    var res = restExchange.apply(Map.of(
                            "requiredTermsAgree", true,
                            "marketingEmailOptIn", true
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("닉네임 길이 제한 초과", () -> {
                    var res = restExchange.apply(Map.of(
                            "nickname", "12345678901",
                            "requiredTermsAgree", true,
                            "marketingEmailOptIn", true
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("필수 약관 동의 누락", () -> {
                    var res = restExchange.apply(Map.of(
                            "nickname", "testName",
                            "marketingEmailOptIn", true
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("필수 약관 동의 false", () -> {
                    var res = restExchange.apply(Map.of(
                            "nickname", "testName",
                            "requiredTermsAgree", false,
                            "marketingEmailOptIn", true
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("마케팅 이메일 수신 동의 누락", () -> {
                    var res = restExchange.apply(Map.of(
                            "nickname", "testName",
                            "requiredTermsAgree", true
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
        );
    }

    @Test
    @DisplayName("회원 정보 수정 - 정상")
    void updateInfo() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();
        GeneratedToken token = testUserCreator.createToken(registeredUser);

        // when
        var res = rest.exchange(
                "/user/info",
                HttpMethod.PUT,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of(
                                "nickname", "newName"
                        ))
                        .build(),
                String.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @TestFactory
    @DisplayName("회원 정보 수정 - 실패")
    Collection<DynamicTest> updateInfoError() {
        // given
        User registeredUser = testUserCreator.createRegisteredUser();
        GeneratedToken token = testUserCreator.createToken(registeredUser);

        Function<Map<String, Object>, ResponseEntity<String>> restExchange = body -> rest.exchange(
                "/user/info",
                HttpMethod.PUT,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(body)
                        .build(),
                String.class
        );

        return List.of(
                DynamicTest.dynamicTest("닉네임 누락", () -> {
                    var res = restExchange.apply(Map.of());
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("닉네임 길이 제한 초과", () -> {
                    var res = restExchange.apply(Map.of(
                            "nickname", "12345678901"
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
        );
    }
}
