package vook.server.api.usecases.term;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.term.exception.TermNotFoundException;
import vook.server.api.domain.term.model.TermRepository;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestTermCreator;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.usecases.common.polices.VocabularyPolicy;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Transactional
class UpdateTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    UpdateTermUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    TestTermCreator testTermCreator;
    @Autowired
    TermRepository termRepository;
    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @DisplayName("용어 수정 - 정상")
    void execute() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);
        var term = testTermCreator.createTerm(vocabulary);

        UpdateTermUseCase.Command command = new UpdateTermUseCase.Command(
                vookLoginUser.getUid(),
                term.getUid(),
                "수정된 용어",
                "수정된 뜻",
                new ArrayList<>()
        );

        // when
        useCase.execute(command);

        // then
        var updatedTerm = termRepository.findByUid(term.getUid()).orElseThrow();
        assertThat(updatedTerm.getTerm()).isEqualTo(command.term());
        assertThat(updatedTerm.getMeaning()).isEqualTo(command.meaning());
        assertThat(updatedTerm.getSynonyms()).isEmpty();
    }

    @Test
    @DisplayName("용어 수정 - 실패; 용어가 존재하지 않는 경우")
    void executeError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());

        UpdateTermUseCase.Command command = new UpdateTermUseCase.Command(
                vookLoginUser.getUid(),
                UUID.randomUUID().toString(),
                "수정된 용어",
                "수정된 뜻",
                new ArrayList<>()
        );

        // when, then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(TermNotFoundException.class);
    }

    @Test
    @DisplayName("용어 수정 - 실패; 사용자가 용어가 소속된 용어집 소유자가 아닌 경우")
    void executeError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);
        var term = testTermCreator.createTerm(vocabulary);

        User anotherUser = testUserCreator.createCompletedOnboardingUser();

        UpdateTermUseCase.Command command = new UpdateTermUseCase.Command(
                anotherUser.getUid(),
                term.getUid(),
                "수정된 용어",
                "수정된 뜻",
                new ArrayList<>()
        );

        // when, then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(VocabularyPolicy.NotValidVocabularyOwnerException.class);
    }
}
