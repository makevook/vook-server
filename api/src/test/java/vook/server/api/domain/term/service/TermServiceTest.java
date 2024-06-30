package vook.server.api.domain.term.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.domain.vocabulary.service.data.TermCreateCommand;
import vook.server.api.globalcommon.exception.ParameterValidateException;
import vook.server.api.infra.search.vocabulary.MeilisearchVocabularySearchService;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Autowired
    MeilisearchVocabularySearchService searchService;

    @AfterEach
    void tearDown() {
        searchService.clearAll();
    }

    @Test
    @DisplayName("용어 생성 - 성공")
    void create() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);

        TermCreateCommand command = TermCreateCommand.builder()
                .vocabularyUid(vocabulary.getUid())
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

        assertThat(searchService.isDocumentExists(vocabulary.getUid(), term.getUid())).isTrue();
    }

    @TestFactory
    @DisplayName("용어 생성 - 실패; 파라미터 룰 위반")
    Collection<DynamicTest> create_ParameterError() {
        return List.of(
                DynamicTest.dynamicTest("용어 이름이 누락 된 경우", () -> {
                    TermCreateCommand command = TermCreateCommand.builder()
                            .meaning("용어 설명")
                            .synonyms(List.of("동의어1", "동의어2"))
                            .build();

                    assertThatThrownBy(() -> service.create(command)).isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("용어 이름이 제한을 넘는 경우", () -> {
                    TermCreateCommand command = TermCreateCommand.builder()
                            .term("a".repeat(101))
                            .meaning("용어 설명")
                            .synonyms(List.of("동의어1", "동의어2"))
                            .build();

                    assertThatThrownBy(() -> service.create(command)).isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("용어 뜻이 누락 된 경우", () -> {
                    TermCreateCommand command = TermCreateCommand.builder()
                            .term("용어")
                            .synonyms(List.of("동의어1", "동의어2"))
                            .build();

                    assertThatThrownBy(() -> service.create(command)).isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("용어 뜻이 제한을 넘는 경우", () -> {
                    TermCreateCommand command = TermCreateCommand.builder()
                            .term("용어")
                            .meaning("a".repeat(2001))
                            .synonyms(List.of("동의어1", "동의어2"))
                            .build();

                    assertThatThrownBy(() -> service.create(command)).isInstanceOf(ParameterValidateException.class);
                }),
                DynamicTest.dynamicTest("동의어가 누락 된 경우", () -> {
                    TermCreateCommand command = TermCreateCommand.builder()
                            .term("용어")
                            .meaning("용어 설명")
                            .build();

                    assertThatThrownBy(() -> service.create(command)).isInstanceOf(ParameterValidateException.class);
                })
        );
    }
}
