package vook.server.api.web.routes.vocabulary;

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
import vook.server.api.app.contexts.user.domain.User;
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
class VocabularyRestControllerTest extends IntegrationTestBase {

    @MockBean
    VocabularyWebService webService;

    @Autowired
    TestUserCreator testUserCreator;

    @Test
    @DisplayName("단어장 생성 - 정상")
    void createVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        GeneratedToken token = testUserCreator.createToken(user);

        // when
        var res = rest.exchange(
                "/vocabularies",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of("name", "단어장 이름"))
                        .build(),
                VocabularyApi.VocabularyApiVocabulariesResponse.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @TestFactory
    @DisplayName("단어장 생성 - 실패")
    Collection<DynamicTest> createVocabularyFail() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        GeneratedToken token = testUserCreator.createToken(user);

        Function<Map<String, Object>, ResponseEntity<String>> restExchange = body -> rest.exchange(
                "/vocabularies",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(body)
                        .build(),
                String.class
        );

        return List.of(
                DynamicTest.dynamicTest("단어장 이름 누락", () -> {
                    var res = restExchange.apply(Map.of());
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("단어장 이름 길이 제한 초과", () -> {
                    var res = restExchange.apply(Map.of("name", "012345678901234567891"));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
        );
    }

    @Test
    @DisplayName("단어장 수정 - 정상")
    void updateVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        GeneratedToken token = testUserCreator.createToken(user);

        // when
        var res = rest.exchange(
                "/vocabularies/1",
                HttpMethod.PUT,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of("name", "단어장 이름"))
                        .build(),
                VocabularyApi.VocabularyApiVocabulariesResponse.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @TestFactory
    @DisplayName("단어장 수정 - 실패")
    Collection<DynamicTest> updateVocabularyFail() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        GeneratedToken token = testUserCreator.createToken(user);

        Function<Map<String, Object>, ResponseEntity<String>> restExchange = body -> rest.exchange(
                "/vocabularies/1",
                HttpMethod.PUT,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(body)
                        .build(),
                String.class
        );

        return List.of(
                DynamicTest.dynamicTest("단어장 이름 누락", () -> {
                    var res = restExchange.apply(Map.of());
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("단어장 이름 길이 제한 초과", () -> {
                    var res = restExchange.apply(Map.of("name", "012345678901234567891"));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
        );
    }
}
