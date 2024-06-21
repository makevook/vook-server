package vook.server.api.web.routes.term;

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
import vook.server.api.app.context.user.domain.User;
import vook.server.api.testhelper.HttpEntityBuilder;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.web.auth.data.GeneratedToken;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class TermRestControllerTest extends IntegrationTestBase {

    @MockBean
    TermWebService webService;

    @Autowired
    TestUserCreator testUserCreator;

    @Test
    @DisplayName("용어 생성 - 정상")
    void create() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        GeneratedToken token = testUserCreator.createToken(user);

        // when
        var res = rest.exchange(
                "/terms",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(Map.of(
                                "vocabularyUid", "vocabularyUid",
                                "term", "term",
                                "meaning", "meaning",
                                "synonyms", List.of("synonym1", "synonym2")
                        ))
                        .build(),
                TermApi.TermApiCreateResponse.class
        );

        // then
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @TestFactory
    @DisplayName("용어 생성 - 실패")
    Collection<DynamicTest> createFail() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        GeneratedToken token = testUserCreator.createToken(user);

        Function<Map<String, Object>, ResponseEntity<Void>> restExchange = body -> rest.exchange(
                "/terms",
                HttpMethod.POST,
                new HttpEntityBuilder()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .body(body)
                        .build(),
                Void.class
        );

        return List.of(
                DynamicTest.dynamicTest("용어집 uid 누락", () -> {
                    var res = restExchange.apply(Map.of(
                            "term", "term",
                            "meaning", "meaning",
                            "synonyms", List.of("synonym1", "synonym2")
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("용어 누락", () -> {
                    var res = restExchange.apply(Map.of(
                            "vocabularyUid", "vocabularyUid",
                            "meaning", "meaning",
                            "synonyms", List.of("synonym1", "synonym2")
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("뜻 누락", () -> {
                    var res = restExchange.apply(Map.of(
                            "vocabularyUid", "vocabularyUid",
                            "term", "term",
                            "synonyms", List.of("synonym1", "synonym2")
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("용어 길이 초과", () -> {
                    var res = restExchange.apply(Map.of(
                            "vocabularyUid", "vocabularyUid",
                            "term", IntStream.range(0, 101).mapToObj(i -> "a").reduce("", String::concat),
                            "meaning", "meaning"
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                }),
                DynamicTest.dynamicTest("뜻 길이 초과", () -> {
                    var res = restExchange.apply(Map.of(
                            "vocabularyUid", "vocabularyUid",
                            "term", "term",
                            "meaning", IntStream.range(0, 2001).mapToObj(i -> "a").reduce("", String::concat)
                    ));
                    assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
        );
    }
}
