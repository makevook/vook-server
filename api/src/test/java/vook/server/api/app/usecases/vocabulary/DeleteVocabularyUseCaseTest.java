package vook.server.api.app.usecases.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.app.polices.VocabularyPolicy;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.auth.data.VookLoginUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class DeleteVocabularyUseCaseTest extends IntegrationTestBase {

    @Autowired
    DeleteVocabularyUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @DisplayName("용어집 삭제 - 정상")
    void deleteVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        // when
        var command = new DeleteVocabularyUseCase.Command(vookLoginUser.getUid(), vocabulary.getUid());
        useCase.execute(command);

        // then
        assertThat(vocabularyRepository.findByUid(vocabulary.getUid())).isEmpty();
    }

    @Test
    @DisplayName("용어집 삭제 - 실패; 해당 용어집이 존재하지 않는 경우")
    void deleteVocabularyError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        testVocabularyCreator.createVocabulary(user);

        // when
        assertThatThrownBy(() -> {
            var command = new DeleteVocabularyUseCase.Command(vookLoginUser.getUid(), "nonExistentUid");
            useCase.execute(command);
        }).isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("용어집 삭제 - 실패; 해당 용어집이 다른 사용자의 것인 경우")
    void deleteVocabularyError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        User otherUser = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(otherUser);

        // when
        assertThatThrownBy(() -> {
            var command = new DeleteVocabularyUseCase.Command(vookLoginUser.getUid(), vocabulary.getUid());
            useCase.execute(command);
        }).isInstanceOf(VocabularyPolicy.NotValidOwnerException.class);
    }
}
