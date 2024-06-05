package vook.server.api.web.routes.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import vook.server.api.app.auth.data.GeneratedToken;
import vook.server.api.model.user.User;
import vook.server.api.model.user.UserStatus;
import vook.server.api.testhelper.HttpEntityBuilder;
import vook.server.api.testhelper.TestDataCreator;
import vook.server.api.testhelper.WebApiTest;

import static org.assertj.core.api.Assertions.assertThat;

class UserRestControllerTest extends WebApiTest {

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
                        .addHeader("Authorization", "Bearer " + token.getAccessToken())
                        .build(),
                UserApi.UserApiUerInfoResponse.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().getResult().getUid()).isEqualTo(unregisteredUser.getUid());
        assertThat(res.getBody().getResult().getEmail()).isEqualTo(unregisteredUser.getEmail());
        assertThat(res.getBody().getResult().getStatus()).isEqualTo(UserStatus.SOCIAL_LOGIN_COMPLETED);
    }
}
