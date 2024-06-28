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

import static org.assertj.core.api.Assertions.assertThat;

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
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createTerm(vocabulary, "하이브리드앱");
        testVocabularyCreator.createTerm(vocabulary, "네이티브앱");

        SearchTermUseCase.Command command = SearchTermUseCase.Command.builder()
                .userUid(user.getUid())
                .vocabularyUid(vocabulary.getUid())
                .query("하이브리드앱")
                .withFormat(true)
                .build();

        // when
        SearchTermUseCase.Result result = searchTermUseCase.execute(command);

        // then
        assertThat(result.query()).isEqualTo("하이브리드앱");
        assertThat(result.hits()).isNotEmpty();
        assertThat(result.hits().getFirst().term()).contains("하이브리드앱");
        assertThat(result.hits().getFirst().meaning()).contains("하이브리드앱");
        assertThat(result.hits().getFirst().synonyms()).contains("하이브리드앱");
    }

}
