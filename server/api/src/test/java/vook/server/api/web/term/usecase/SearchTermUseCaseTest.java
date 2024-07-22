package vook.server.api.web.term.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.policy.VocabularyPolicy;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static vook.server.api.testhelper.creator.TestVocabularyCreator.TermInfo;

@Transactional
class SearchTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    SearchTermUseCase searchTermUseCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator vocaCreator;

    @Test
    @DisplayName("용어 검색 - 정상")
    void searchTerms() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary1 = vocaCreator.createVocabulary(user);
        vocaCreator.createTerm(vocabulary1, "하이브리드앱");
        vocaCreator.createTerm(vocabulary1, "네이티브앱");
        Vocabulary vocabulary2 = vocaCreator.createVocabulary(user);
        vocaCreator.createTerm(vocabulary2, "하이브리드웹");
        vocaCreator.createTerm(vocabulary2, "네이티브웹");

        SearchTermUseCase.Command command = SearchTermUseCase.Command.builder()
                .userUid(user.getUid())
                .vocabularyUids(List.of(vocabulary1.getUid(), vocabulary2.getUid()))
                .queries(List.of("하이브리드"))
                .build();

        // when
        SearchTermUseCase.Result result = searchTermUseCase.execute(command);

        // then
        assertThat(result.records())
                .isNotEmpty()
                .satisfiesExactlyInAnyOrder(
                        term -> {
                            assertThat(term.vocabularyUid()).isEqualTo(vocabulary1.getUid());
                            assertThat(term.query()).isEqualTo("하이브리드");
                            assertThat(term.hits()).hasSize(1);
                            assertThat(term.hits().getFirst().term()).contains("하이브리드앱");
                            assertThat(term.hits().getFirst().meaning()).contains("하이브리드앱");
                            assertThat(term.hits().getFirst().synonyms()).contains("하이브리드앱");
                        },
                        term -> {
                            assertThat(term.vocabularyUid()).isEqualTo(vocabulary2.getUid());
                            assertThat(term.query()).isEqualTo("하이브리드");
                            assertThat(term.hits()).hasSize(1);
                            assertThat(term.hits().getFirst().term()).contains("하이브리드웹");
                            assertThat(term.hits().getFirst().meaning()).contains("하이브리드웹");
                            assertThat(term.hits().getFirst().synonyms()).contains("하이브리드웹");
                        }
                );
    }

    @Test
    @DisplayName("용어 검색 - 실패; 사용자가 소유한 용어집이 아닌 경우")
    void searchTerms_userDoesNotOwnVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        vocaCreator.createVocabulary(user);
        vocaCreator.createVocabulary(user);

        User anotherUser = testUserCreator.createCompletedOnboardingUser();
        Vocabulary anotherVocabulary = vocaCreator.createVocabulary(anotherUser);

        SearchTermUseCase.Command command = SearchTermUseCase.Command.builder()
                .userUid(user.getUid())
                .vocabularyUids(List.of(anotherVocabulary.getUid()))
                .queries(List.of("하이브리드"))
                .build();

        // when, then
        assertThatThrownBy(() -> searchTermUseCase.execute(command))
                .isInstanceOf(VocabularyPolicy.NotValidVocabularyOwnerException.class);
    }

    @Test
    @DisplayName("용어 검색 - 검색 쿼리내 단어가 몇 개이던, 첫번째 단어 기준으로 검색이 된다.")
    void searchTerms_queryIsTooLong() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary voca = vocaCreator.createVocabulary(user);
        vocaCreator.createTerms(voca, List.of(
                new TermInfo("비빔밥", "비빔밥", List.of("비빔밥")),
                new TermInfo("김치찌개", "김치찌개", List.of("김치찌개")),
                new TermInfo("불고기", "불고기", List.of("불고기"))
        ));

        SearchTermUseCase.Command command = SearchTermUseCase.Command.builder()
                .userUid(user.getUid())
                .vocabularyUids(List.of(voca.getUid()))
                .queries(List.of("김치찌개와 비빔밥, 그리고 불고기"))
                .build();

        // when
        SearchTermUseCase.Result result = searchTermUseCase.execute(command);

        // then
        assertThat(result.records())
                .isNotEmpty()
                .satisfiesExactlyInAnyOrder(
                        term -> {
                            assertThat(term.vocabularyUid()).isEqualTo(voca.getUid());
                            assertThat(term.hits()).hasSize(1);
                            assertThat(term.query()).isEqualTo("김치찌개와 비빔밥, 그리고 불고기");
                            assertThat(term.hits().getFirst().term()).contains("김치찌개");
                            assertThat(term.hits().getFirst().meaning()).contains("김치찌개");
                            assertThat(term.hits().getFirst().synonyms()).contains("김치찌개");
                        }
                );
    }
}
