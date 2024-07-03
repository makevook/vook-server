package vook.server.api.usecases.term;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class SearchTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    SearchTermUseCase searchTermUseCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;

    @Test
    @DisplayName("용어 검색 - 정상")
    void searchTerms() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary1 = testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createTerm(vocabulary1, "하이브리드앱");
        testVocabularyCreator.createTerm(vocabulary1, "네이티브앱");
        Vocabulary vocabulary2 = testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createTerm(vocabulary2, "하이브리드웹");
        testVocabularyCreator.createTerm(vocabulary2, "네이티브웹");

        SearchTermUseCase.Command command = SearchTermUseCase.Command.builder()
                .userUid(user.getUid())
                .vocabularyUids(List.of(vocabulary1.getUid(), vocabulary2.getUid()))
                .query("하이브리드")
                .build();

        // when
        SearchTermUseCase.Result result = searchTermUseCase.execute(command);

        // then
        assertThat(result.query()).isEqualTo("하이브리드");
        assertThat(result.records())
                .isNotEmpty()
                .satisfiesExactlyInAnyOrder(
                        term -> {
                            assertThat(term.vocabularyUid()).isEqualTo(vocabulary1.getUid());
                            assertThat(term.hits()).hasSize(1);
                            assertThat(term.hits().getFirst().term()).contains("하이브리드앱");
                            assertThat(term.hits().getFirst().meaning()).contains("하이브리드앱");
                            assertThat(term.hits().getFirst().synonyms()).contains("하이브리드앱");
                        },
                        term -> {
                            assertThat(term.vocabularyUid()).isEqualTo(vocabulary2.getUid());
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
        testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createVocabulary(user);

        User anotherUser = testUserCreator.createCompletedOnboardingUser();
        Vocabulary anotherVocabulary = testVocabularyCreator.createVocabulary(anotherUser);

        SearchTermUseCase.Command command = SearchTermUseCase.Command.builder()
                .userUid(user.getUid())
                .vocabularyUids(List.of(anotherVocabulary.getUid()))
                .query("하이브리드")
                .build();

        // when, then
        assertThatThrownBy(() -> searchTermUseCase.execute(command))
                .isInstanceOf(VocabularyPolicy.NotValidVocabularyOwnerException.class);
    }
}
