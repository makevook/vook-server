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
import vook.server.api.web.routes.vocabulary.reqres.VocabularyUpdateRequest;

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
    @DisplayName("단어장 수정 - 정상")
    void updateVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        var request = new VocabularyUpdateRequest();
        request.setName("updatedName");

        // when
        var command = new UpdateVocabularyUseCase.Command(vookLoginUser.getUid(), vocabulary.getUid(), request.getName());
        useCase.execute(command);

        // then
        Vocabulary updatedVocabulary = vocabularyRepository.findByUid(vocabulary.getUid()).orElseThrow();
        assertThat(updatedVocabulary.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("단어장 수정 - 실패; 해당 단어장이 존재하지 않는 경우")
    void updateVocabularyError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        testVocabularyCreator.createVocabulary(user);

        var request = new VocabularyUpdateRequest();
        request.setName("updatedName");

        // when
        assertThatThrownBy(() -> {
            var command = new UpdateVocabularyUseCase.Command(vookLoginUser.getUid(), "nonExistentUid", request.getName());
            useCase.execute(command);
        }).isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("단어장 수정 - 실패; 해당 단어장이 다른 사용자의 것인 경우")
    void updateVocabularyError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        User otherUser = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(otherUser);

        var request = new VocabularyUpdateRequest();
        request.setName("updatedName");

        // when
        assertThatThrownBy(() -> {
            var command = new UpdateVocabularyUseCase.Command(vookLoginUser.getUid(), vocabulary.getUid(), request.getName());
            useCase.execute(command);
        }).isInstanceOf(VocabularyPolicy.NotValidOwnerException.class);
    }
}
