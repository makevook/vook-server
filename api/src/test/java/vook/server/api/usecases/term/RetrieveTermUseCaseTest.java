package vook.server.api.usecases.term;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.TermSynonym;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class RetrieveTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    RetrieveTermUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("용어 조회 - 정상; 기본")
    void execute() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);
        Term term = testVocabularyCreator.createTerm(vocabulary);

        RetrieveTermUseCase.Command command = new RetrieveTermUseCase.Command(
                user.getUid(),
                vocabulary.getUid(),
                PageRequest.ofSize(Integer.MAX_VALUE)
        );

        // when
        RetrieveTermUseCase.Result result = useCase.execute(command);

        // then
        assertThat(result.terms().getContent()).hasSize(1);
        assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term.getUid());
        assertThat(result.terms().getContent().get(0).term()).isEqualTo(term.getTerm());
        assertThat(result.terms().getContent().get(0).meaning()).isEqualTo(term.getMeaning());
        assertThat(result.terms().getContent().get(0).synonym()).containsExactlyInAnyOrderElementsOf(
                term.getSynonyms().stream().map(TermSynonym::getSynonym).toList()
        );
    }

    @TestFactory
    @DisplayName("용어 조회 - 정상; 정렬")
    Collection<DynamicTest> executeSort() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);
        Term term1 = testVocabularyCreator.createTerm(vocabulary, "1");
        Term term2 = testVocabularyCreator.createTerm(vocabulary, "2");
        Term term3 = testVocabularyCreator.createTerm(vocabulary, "3");

        return List.of(
                DynamicTest.dynamicTest("용어 이름 오름차순", () -> {
                    // when
                    RetrieveTermUseCase.Result result = useCase.execute(new RetrieveTermUseCase.Command(
                            user.getUid(),
                            vocabulary.getUid(),
                            PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "term")
                    ));

                    // then
                    assertThat(result.terms().getContent()).hasSize(3);
                    assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term1.getUid());
                    assertThat(result.terms().getContent().get(1).termUid()).isEqualTo(term2.getUid());
                    assertThat(result.terms().getContent().get(2).termUid()).isEqualTo(term3.getUid());
                }),
                DynamicTest.dynamicTest("용어 이름 내림차순", () -> {
                    // when
                    RetrieveTermUseCase.Result result = useCase.execute(new RetrieveTermUseCase.Command(
                            user.getUid(),
                            vocabulary.getUid(),
                            PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "term")
                    ));

                    // then
                    assertThat(result.terms().getContent()).hasSize(3);
                    assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term3.getUid());
                    assertThat(result.terms().getContent().get(1).termUid()).isEqualTo(term2.getUid());
                    assertThat(result.terms().getContent().get(2).termUid()).isEqualTo(term1.getUid());
                }),
                DynamicTest.dynamicTest("뜻 오름차순", () -> {
                    // when
                    RetrieveTermUseCase.Result result = useCase.execute(new RetrieveTermUseCase.Command(
                            user.getUid(),
                            vocabulary.getUid(),
                            PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "meaning")
                    ));

                    // then
                    assertThat(result.terms().getContent()).hasSize(3);
                    assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term1.getUid());
                    assertThat(result.terms().getContent().get(1).termUid()).isEqualTo(term2.getUid());
                    assertThat(result.terms().getContent().get(2).termUid()).isEqualTo(term3.getUid());
                }),
                DynamicTest.dynamicTest("뜻 내림차순", () -> {
                    // when
                    RetrieveTermUseCase.Result result = useCase.execute(new RetrieveTermUseCase.Command(
                            user.getUid(),
                            vocabulary.getUid(),
                            PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "meaning")
                    ));

                    // then
                    assertThat(result.terms().getContent()).hasSize(3);
                    assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term3.getUid());
                    assertThat(result.terms().getContent().get(1).termUid()).isEqualTo(term2.getUid());
                    assertThat(result.terms().getContent().get(2).termUid()).isEqualTo(term1.getUid());
                }),
                DynamicTest.dynamicTest("생성 일차 오름차순", () -> {
                    // when
                    RetrieveTermUseCase.Result result = useCase.execute(new RetrieveTermUseCase.Command(
                            user.getUid(),
                            vocabulary.getUid(),
                            PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "createdAt")
                    ));

                    // then
                    assertThat(result.terms().getContent()).hasSize(3);
                    assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term1.getUid());
                    assertThat(result.terms().getContent().get(1).termUid()).isEqualTo(term2.getUid());
                    assertThat(result.terms().getContent().get(2).termUid()).isEqualTo(term3.getUid());
                }),
                DynamicTest.dynamicTest("생성 일차 내림차순", () -> {
                    // when
                    RetrieveTermUseCase.Result result = useCase.execute(new RetrieveTermUseCase.Command(
                            user.getUid(),
                            vocabulary.getUid(),
                            PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "createdAt")
                    ));

                    // then
                    assertThat(result.terms().getContent()).hasSize(3);
                    assertThat(result.terms().getContent().get(0).termUid()).isEqualTo(term3.getUid());
                    assertThat(result.terms().getContent().get(1).termUid()).isEqualTo(term2.getUid());
                    assertThat(result.terms().getContent().get(2).termUid()).isEqualTo(term1.getUid());
                })
        );
    }

}
