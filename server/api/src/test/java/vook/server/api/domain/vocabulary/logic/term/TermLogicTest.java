package vook.server.api.domain.vocabulary.logic.term;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.user.User;
import vook.server.api.domain.vocabulary.exception.TermNotFoundException;
import vook.server.api.domain.vocabulary.model.term.Term;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.globalcommon.exception.ParameterValidateException;
import vook.server.api.infra.search.vocabulary.MeilisearchSearchManagementService;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
class TermLogicTest extends IntegrationTestBase {

    @Autowired
    TermLogic service;

    @Autowired
    TestUserCreator userCreator;
    @Autowired
    TestVocabularyCreator vocabularyCreator;

    @Autowired
    MeilisearchSearchManagementService searchService;
    @Autowired
    TermRepository termRepository;

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
        Map document = searchService.getDocument(vocabulary.getUid(), term.getUid());
        assertEquals("용어", document.get("term"));
        assertEquals("용어 설명", document.get("meaning"));
        assertEquals("동의어1,동의어2", document.get("synonyms"));
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

    @Test
    @DisplayName("용어 뭉치 생성 - 성공")
    void createAll() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);

        TermCreateAllCommand command = TermCreateAllCommand.builder()
                .vocabularyUid(vocabulary.getUid())
                .termInfos(List.of(
                        TermCreateAllCommand.TermInfo.builder()
                                .term("용어1")
                                .meaning("용어 설명1")
                                .synonyms(List.of("동의어1", "동의어2"))
                                .build(),
                        TermCreateAllCommand.TermInfo.builder()
                                .term("용어2")
                                .meaning("용어 설명2")
                                .synonyms(List.of("동의어3", "동의어4"))
                                .build()
                ))
                .build();

        // when
        service.createAll(command);

        // then
        List<Term> terms = termRepository.findAll();
        assertEquals(2, terms.size());
        assertEquals("용어1", terms.get(0).getTerm());
        assertEquals("용어 설명1", terms.get(0).getMeaning());
        assertEquals(2, terms.get(0).getSynonyms().size());
        assertEquals("용어2", terms.get(1).getTerm());
        assertEquals("용어 설명2", terms.get(1).getMeaning());
        assertEquals(2, terms.get(1).getSynonyms().size());

        assertThat(searchService.isDocumentExists(vocabulary.getUid(), terms.get(0).getUid())).isTrue();
        Map document1 = searchService.getDocument(vocabulary.getUid(), terms.get(0).getUid());
        assertEquals("용어1", document1.get("term"));
        assertEquals("용어 설명1", document1.get("meaning"));
        assertEquals("동의어1,동의어2", document1.get("synonyms"));

        assertThat(searchService.isDocumentExists(vocabulary.getUid(), terms.get(1).getUid())).isTrue();
        Map document2 = searchService.getDocument(vocabulary.getUid(), terms.get(1).getUid());
        assertEquals("용어2", document2.get("term"));
        assertEquals("용어 설명2", document2.get("meaning"));
        assertEquals("동의어3,동의어4", document2.get("synonyms"));
    }

    @Test
    @DisplayName("용어 수정 - 성공")
    void update() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);
        Term term = vocabularyCreator.createTerm(vocabulary);

        TermUpdateCommand command = TermUpdateCommand.builder()
                .uid(term.getUid())
                .term("수정된 용어")
                .meaning("수정된 용어 설명")
                .synonyms(List.of("수정된 동의어1", "수정된 동의어2"))
                .build();

        // when
        service.update(command);

        // then
        Term updated = service.getByUid(term.getUid());
        assertEquals("수정된 용어", updated.getTerm());
        assertEquals("수정된 용어 설명", updated.getMeaning());
        assertEquals(2, updated.getSynonyms().size());

        assertThat(searchService.isDocumentExists(vocabulary.getUid(), term.getUid())).isTrue();
        Map document = searchService.getDocument(vocabulary.getUid(), term.getUid());
        assertEquals("수정된 용어", document.get("term"));
        assertEquals("수정된 용어 설명", document.get("meaning"));
        assertEquals("수정된 동의어1,수정된 동의어2", document.get("synonyms"));
    }

    @Test
    @DisplayName("용어 삭제 - 성공")
    void delete() {
        // given
        User user = userCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = vocabularyCreator.createVocabulary(user);
        Term term = vocabularyCreator.createTerm(vocabulary);
        assertThat(searchService.isDocumentExists(vocabulary.getUid(), term.getUid())).isTrue();

        // when
        service.delete(term.getUid());

        // then
        assertThatThrownBy(() -> service.getByUid(term.getUid())).isInstanceOf(TermNotFoundException.class);
        assertThat(searchService.isDocumentExists(vocabulary.getUid(), term.getUid())).isFalse();
    }
}
