package vook.server.api.domain.vocabulary.model.term;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.domain.vocabulary.model.vocabulary.VocabularyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DefaultTermFactory.class)
@ContextConfiguration(classes = ValidationAutoConfiguration.class)
class TermFactoryTest {

    @MockBean
    private VocabularyRepository vocabularyRepository;

    @Autowired
    private TermFactory factory;

    @Test
    @DisplayName("용어 생성; 정상")
    void create() {
        // given
        Vocabulary vocabulary = mock(Vocabulary.class);
        when(vocabulary.termCount()).thenReturn(0);
        when(vocabularyRepository.findByUid(anyString())).thenReturn(Optional.of(vocabulary));

        // when
        Term term = factory.create(
                new TermFactory.CreateCommand(
                        "vocabularyUid",
                        new TermFactory.TermInfo("term", "meaning", List.of("synonym"))
                )
        );

        // then
        assertTermForCreate(term, vocabulary, "term", "meaning", List.of("synonym"));
    }

    @Test
    @DisplayName("용어 생성; 예외 - uid에 해당하는 용어집이 없는 경우")
    void createFail1() {
        // given
        when(vocabularyRepository.findByUid(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> factory.create(
                new TermFactory.CreateCommand(
                        "vocabularyUid",
                        new TermFactory.TermInfo("term", "meaning", List.of("synonym"))
                )
        )).isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("용어 생성; 예외 - 용어집의 용어 개수 초과")
    void createFail2() {
        // given
        Vocabulary vocabulary = mock(Vocabulary.class);
        when(vocabulary.termCount()).thenReturn(100);
        when(vocabularyRepository.findByUid(anyString())).thenReturn(Optional.of(vocabulary));

        // when & then
        assertThatThrownBy(() -> factory.create(
                new TermFactory.CreateCommand(
                        "vocabularyUid",
                        new TermFactory.TermInfo("term", "meaning", List.of("synonym"))
                )
        )).isInstanceOf(TermLimitExceededException.class);
    }

    @TestFactory
    @DisplayName("용어 생성; 예외 - 용어 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createFail3() {
        return Stream.of(
                dynamicTest("용어가 없는 경우", () -> {
                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("", "meaning", List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("용어가 100자 초과인 경우", () -> {
                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("a".repeat(101), "meaning", List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("의미가 없는 경우", () -> {
                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("term", "", List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("의미가 2000자 초과인 경우", () -> {
                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("term", "a".repeat(2001), List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("동의어가 null인 경우", () -> {
                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("term", "meaning", null)
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("동의어가 합쳤을 때 콤마 포함 2000자가 넘는 경우", () -> {
                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("term", "meaning", List.of("a".repeat(2001)))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);

                    assertThatThrownBy(() -> factory.create(
                            new TermFactory.CreateCommand(
                                    "vocabularyUid",
                                    new TermFactory.TermInfo("term", "meaning", List.of("a".repeat(1000), "b".repeat(1000)))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                })
        );
    }

    @Test
    @DisplayName("배치 생성을 위한 용어 생성; 정상")
    void createForBatchCreate() {
        // given
        Vocabulary vocabulary = mock(Vocabulary.class);
        when(vocabulary.termCount()).thenReturn(0);
        when(vocabularyRepository.findByUid(anyString())).thenReturn(Optional.of(vocabulary));

        // when
        List<Term> terms = factory.createForBatchCreate(
                new TermFactory.CreateForBatchCommand(
                        "vocabularyUid",
                        List.of(
                                new TermFactory.TermInfo("term1", "meaning1", List.of("synonym1")),
                                new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                        )
                )
        );

        // then
        assertThat(terms).hasSize(2);
        assertTermForCreate(terms.get(0), vocabulary, "term1", "meaning1", List.of("synonym1"));
        assertTermForCreate(terms.get(1), vocabulary, "term2", "meaning2", List.of("synonym2"));
    }

    @Test
    @DisplayName("배치 생성을 위한 용어 생성; 예외 - uid에 해당하는 용어집이 없는 경우")
    void createForBatchCreateFail1() {
        // given
        when(vocabularyRepository.findByUid(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> factory.createForBatchCreate(
                new TermFactory.CreateForBatchCommand(
                        "vocabularyUid",
                        List.of(
                                new TermFactory.TermInfo("term1", "meaning1", List.of("synonym1")),
                                new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                        )
                )
        )).isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("배치 생성을 위한 용어 생성; 예외 - 용어집의 용어 개수 초과")
    void createForBatchCreateFail2() {
        // given
        Vocabulary vocabulary = mock(Vocabulary.class);
        when(vocabulary.termCount()).thenReturn(99);
        when(vocabularyRepository.findByUid(anyString())).thenReturn(Optional.of(vocabulary));

        // when & then
        assertThatThrownBy(() -> factory.createForBatchCreate(
                new TermFactory.CreateForBatchCommand(
                        "vocabularyUid",
                        List.of(
                                new TermFactory.TermInfo("term1", "meaning1", List.of("synonym1")),
                                new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                        )
                )
        )).isInstanceOf(TermLimitExceededException.class);
    }

    @TestFactory
    @DisplayName("배치 생성을 위한 용어 생성; 예외 - 용어 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createForBatchCreateFail3() {
        return Stream.of(
                dynamicTest("용어가 없는 경우", () -> {
                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("", "meaning1", List.of("synonym1")),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("용어가 100자 초과인 경우", () -> {
                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("a".repeat(101), "meaning1", List.of("synonym1")),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("의미가 없는 경우", () -> {
                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("term1", "", List.of("synonym1")),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("의미가 2000자 초과인 경우", () -> {
                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("term1", "a".repeat(2001), List.of("synonym1")),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("동의어가 null인 경우", () -> {
                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("term1", "meaning2", null),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of())
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("동의어가 합쳤을 때 콤마 포함 2000자가 넘는 경우", () -> {
                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("term1", "meaning1", List.of("a".repeat(2001))),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);

                    assertThatThrownBy(() -> factory.createForBatchCreate(
                            new TermFactory.CreateForBatchCommand(
                                    "vocabularyUid",
                                    List.of(
                                            new TermFactory.TermInfo("term1", "meaning1", List.of("a".repeat(1000), "b".repeat(1000))),
                                            new TermFactory.TermInfo("term2", "meaning2", List.of("synonym2"))
                                    )
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                })
        );
    }

    @Test
    @DisplayName("용어 수정을 위한 용어 생성; 정상")
    void createForUpdate() {
        // when
        Term term = factory.createForUpdate(
                new TermFactory.UpdateCommand(
                        new TermFactory.TermInfo("term", "meaning", List.of("synonym"))
                )
        );

        // then
        ObjectAssert<Term> termAssert = assertThat(term);
        termAssert.isNotNull();
        termAssert.extracting(Term::getTerm).isEqualTo("term");
        termAssert.extracting(Term::getMeaning).isEqualTo("meaning");
        termAssert.extracting(Term::getSynonyms).isEqualTo(List.of("synonym"));
    }

    @TestFactory
    @DisplayName("용어 수정을 위한 용어 생성; 예외 - 용어 생성 시 유효성 검사 실패")
    Stream<DynamicTest> createForUpdateFail1() {
        return Stream.of(
                dynamicTest("용어가 없는 경우", () -> {
                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("", "meaning", List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("용어가 100자 초과인 경우", () -> {
                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("a".repeat(101), "meaning", List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("의미가 없는 경우", () -> {
                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("term", "", List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("의미가 2000자 초과인 경우", () -> {
                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("term", "a".repeat(2001), List.of("synonym"))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("동의어가 null인 경우", () -> {
                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("term", "meaning", null)
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                }),
                dynamicTest("동의어가 합쳤을 때 콤마 포함 2000자가 넘는 경우", () -> {
                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("term", "meaning", List.of("a".repeat(2001)))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);

                    assertThatThrownBy(() -> factory.createForUpdate(
                            new TermFactory.UpdateCommand(
                                    new TermFactory.TermInfo("term", "meaning", List.of("a".repeat(1000), "b".repeat(1000)))
                            )
                    )).isInstanceOf(ConstraintViolationException.class);
                })
        );
    }

    private void assertTermForCreate(Term target, Vocabulary vocabulary, String term, String meaning, List<String> synonyms) {
        ObjectAssert<Term> termAssert = assertThat(target);
        termAssert.isNotNull();
        termAssert.extracting(Term::getId).isNull();
        termAssert.extracting(Term::getUid).isNotNull();
        termAssert.extracting(Term::getTerm).isEqualTo(term);
        termAssert.extracting(Term::getMeaning).isEqualTo(meaning);
        termAssert.extracting(Term::getSynonyms).isEqualTo(synonyms);
        termAssert.extracting(Term::getVocabulary).isEqualTo(vocabulary);
    }
}
