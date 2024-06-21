package vook.server.api.app.contexts.term.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.term.application.data.TermCreateCommand;
import vook.server.api.app.contexts.term.domain.Term;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
class TermServiceTest extends IntegrationTestBase {

    @Autowired
    TermService service;

    @Autowired
    TestUserCreator userCreator;
    @Autowired
    TestVocabularyCreator vocabularyCreator;

    @Test
    @DisplayName("용어 생성 - 성공")
    void create() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);

        TermCreateCommand command = TermCreateCommand.builder()
                .vocabulary(vocabulary)
                .term("용어")
                .meaning("용어 설명")
                .synonyms(List.of("동의어1", "동의어2"))
                .build();

        // when
        Term term = service.create(command);

        // then
        assertNotNull(term);
        assertEquals("용어", term.getTerm());
        assertEquals("용어 설명", term.getMeaning());
        assertEquals(2, term.getSynonyms().size());
    }

    @Test
    @DisplayName("용어 생성 - 실패; 용어 이름 누락")
    void create_Fail_MissingTerm() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);

        TermCreateCommand command = TermCreateCommand.builder()
                .vocabulary(vocabulary)
                .meaning("용어 설명")
                .synonyms(List.of("동의어1", "동의어2"))
                .build();

        // when & then
        assertThatThrownBy(() -> service.create(command)).isInstanceOf(IllegalArgumentException.class);
    }

}
