package vook.server.api.domain.vocabulary.logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.logic.dto.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.infra.search.vocabulary.MeilisearchVocabularySearchService;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class VocabularyLogicTest extends IntegrationTestBase {

    @Autowired
    VocabularyLogic service;

    @Autowired
    TestUserCreator userCreator;
    @Autowired
    TestVocabularyCreator vocabularyCreator;
    @Autowired
    MeilisearchVocabularySearchService searchService;
    @Autowired
    TermRepository termRepository;

    @AfterEach
    void tearDown() {
        searchService.clearAll();
    }

    @Test
    @DisplayName("단어장 생성 - 성공")
    void create() {
        // given
        User user = userCreator.createCompletedOnboardingUser();

        VocabularyCreateCommand command = VocabularyCreateCommand.builder()
                .name("단어장 이름")
                .userUid(new UserUid(user.getUid()))
                .build();

        // when
        Vocabulary vocabulary = service.create(command);

        // then
        assertThat(vocabulary.getId()).isNotNull();
        assertThat(vocabulary.getUid()).isNotNull();
        assertThat(vocabulary.getName()).isEqualTo("단어장 이름");
        assertThat(vocabulary.getUserUid()).isEqualTo(new UserUid(user.getUid()));

        assertThat(searchService.isIndexExists(vocabulary.getUid())).isTrue();
    }

    @Test
    @DisplayName("단어장 삭제 - 성공")
    void delete() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);

        // when
        service.delete(vocabulary.getUid());

        // then
        assertThatThrownBy(() -> service.getByUid(vocabulary.getUid())).isInstanceOf(VocabularyNotFoundException.class);
        assertThat(searchService.isIndexExists(vocabulary.getUid())).isFalse();
    }

    @Test
    @DisplayName("단어장 삭제 - 성공; 단어들도 같이 삭제")
    void delete_with_terms() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);
        Term term1 = vocabularyCreator.createTerm(vocabulary);
        Term term2 = vocabularyCreator.createTerm(vocabulary);

        // when
        service.delete(vocabulary.getUid());

        // then
        assertThatThrownBy(() -> service.getByUid(vocabulary.getUid())).isInstanceOf(VocabularyNotFoundException.class);
        assertThat(termRepository.findByUid(term1.getUid())).isEmpty();
        assertThat(termRepository.findByUid(term2.getUid())).isEmpty();
        assertThat(searchService.isIndexExists(vocabulary.getUid())).isFalse();
    }
}
