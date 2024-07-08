package vook.server.api.web.user.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.UserRepository;
import vook.server.api.domain.user.model.UserStatus;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class WithdrawUserUseCaseTest extends IntegrationTestBase {

    @Autowired
    WithdrawUserUseCase withdrawUserUseCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;

    @Autowired
    UserRepository userRepository;
    @Autowired
    VocabularyRepository vocabularyRepository;
    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("유저 탈퇴 - 정상")
    void withdrawUser() {
        // given
        var user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createTerm(vocabulary);
        testVocabularyCreator.createTerm(vocabulary);

        var command = new WithdrawUserUseCase.Command(user.getUid());

        // when
        withdrawUserUseCase.execute(command);

        // then
        var savedUser = userRepository.findByUid(user.getUid()).orElseThrow();
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.WITHDRAWN);

        var savedVocabularies = vocabularyRepository.findAllByUserUid(new UserUid(savedUser.getUid()));
        assertThat(savedVocabularies).isEmpty();

        var savedTerms = termRepository.findAll();
        assertThat(savedTerms).isEmpty();
    }
}
