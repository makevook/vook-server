package vook.server.api.web.vocabulary.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.domain.vocabulary.model.vocabulary.UserUid;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.domain.vocabulary.model.vocabulary.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.common.auth.data.VookLoginUser;

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
    @DisplayName("용어집 생성 - 정상")
    void createVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());

        // when
        var command = new CreateVocabularyUseCase.Command(vookLoginUser.getUid(), "testVocabulary");
        useCase.execute(command);

        // then
        List<Vocabulary> vocabularies = vocabularyRepository.findAllByUserUid(new UserUid(user.getUid()));
        assertThat(vocabularies).hasSize(1);
        assertThat(vocabularies.getFirst().getName()).isEqualTo("testVocabulary");
    }

    @Test
    @DisplayName("용어집 생성 - 실패; 용어집이 이미 3개 존재하는 경우")
    void createVocabularyError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());

        // 용어집 3개 생성
        testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createVocabulary(user);
        testVocabularyCreator.createVocabulary(user);

        // when
        assertThatThrownBy(() -> {
            var command = new CreateVocabularyUseCase.Command(vookLoginUser.getUid(), "testVocabulary");
            useCase.execute(command);
        }).isInstanceOf(VocabularyLimitExceededException.class);
    }
}
