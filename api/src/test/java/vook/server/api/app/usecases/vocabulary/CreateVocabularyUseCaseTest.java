package vook.server.api.app.usecases.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CreateVocabularyUseCaseTest extends IntegrationTestBase {

    @Autowired
    CreateVocabularyUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @DisplayName("단어장 생성 - 정상")
    void createVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        var request = new VocabularyCreateRequest();
        request.setName("testVocabulary");

        // when
        var command = new CreateVocabularyUseCase.Command(vookLoginUser.getUid(), request.getName());
        useCase.execute(command);

        // then
        List<Vocabulary> vocabularies = vocabularyRepository.findAllByUser(user);
        assertThat(vocabularies).hasSize(1);
        assertThat(vocabularies.getFirst().getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("단어장 생성 - 실패; 단어장이 이미 3개 존재하는 경우")
    void createVocabularyError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());

        // 단어장 3개 생성
        testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createVocabulary(user);

        var request = new VocabularyCreateRequest();
        request.setName("testVocabulary");

        // when
        assertThatThrownBy(() -> {
            var command = new CreateVocabularyUseCase.Command(vookLoginUser.getUid(), request.getName());
            useCase.execute(command);
        }).isInstanceOf(VocabularyLimitExceededException.class);
    }
}
