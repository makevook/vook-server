package vook.server.api.usecases.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.usecases.common.polices.VocabularyPolicy;
import vook.server.api.web.common.auth.data.VookLoginUser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class UpdateVocabularyUseCaseTest extends IntegrationTestBase {

    @Autowired
    UpdateVocabularyUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @DisplayName("용어집 수정 - 정상")
    void updateVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        // when
        var command = new UpdateVocabularyUseCase.Command(vookLoginUser.getUid(), vocabulary.getUid(), "updatedName");
        useCase.execute(command);

        // then
        Vocabulary updatedVocabulary = vocabularyRepository.findByUid(vocabulary.getUid()).orElseThrow();
        assertThat(updatedVocabulary.getName()).isEqualTo("updatedName");
    }

    @Test
    @DisplayName("용어집 수정 - 실패; 해당 용어집이 존재하지 않는 경우")
    void updateVocabularyError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        testVocabularyCreator.createVocabulary(user);

        // when
        assertThatThrownBy(() -> {
            var command = new UpdateVocabularyUseCase.Command(vookLoginUser.getUid(), "nonExistentUid", "updatedName");
            useCase.execute(command);
        }).isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("용어집 수정 - 실패; 해당 용어집이 다른 사용자의 것인 경우")
    void updateVocabularyError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        User otherUser = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(otherUser);

        // when
        assertThatThrownBy(() -> {
            var command = new UpdateVocabularyUseCase.Command(vookLoginUser.getUid(), vocabulary.getUid(), "updatedName");
            useCase.execute(command);
        }).isInstanceOf(VocabularyPolicy.NotValidVocabularyOwnerException.class);
    }
}
