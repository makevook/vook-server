package vook.server.api.web.term.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.vocabulary.exception.TermNotFoundException;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class DeleteTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    DeleteTermUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("용어 삭제 - 정상")
    void execute() {
        // given
        var user = testUserCreator.createCompletedOnboardingUser();
        var vocabulary = testVocabularyCreator.createVocabulary(user);
        var term = testVocabularyCreator.createTerm(vocabulary);

        var command = new DeleteTermUseCase.Command(
                user.getUid(),
                term.getUid()
        );

        // when
        useCase.execute(command);

        // then
        assertThat(termRepository.findByUid(term.getUid())).isEmpty();
        assertThat(vocabulary.termCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("용어 삭제 - 용어가 없는 경우")
    void executeError1() {
        // given
        var user = testUserCreator.createCompletedOnboardingUser();

        var command = new DeleteTermUseCase.Command(
                user.getUid(),
                "존재하지 않는 용어 uid"
        );

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(TermNotFoundException.class);
    }
}
