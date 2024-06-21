package vook.server.api.web.routes.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyUpdateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class VocabularyWebServiceTest extends IntegrationTestBase {

    @Autowired
    VocabularyWebService vocabularyWebService;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @DisplayName("단어장 조회 - 정상")
    void vocabularies() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        // when
        List<VocabularyResponse> vocabularies = vocabularyWebService.vocabularies(vookLoginUser);

        // then
        assertThat(vocabularies).hasSize(1);
        assertThat(vocabularies.getFirst().getUid()).isEqualTo(vocabulary.getUid());
        assertThat(vocabularies.getFirst().getName()).isEqualTo(vocabulary.getName());
        assertThat(vocabularies.getFirst().getTermCount()).isEqualTo(vocabulary.termCount());
        assertThat(vocabularies.getFirst().getCreatedAt()).isEqualTo(vocabulary.getCreatedAt());
    }

    @Test
    @DisplayName("단어장 생성 - 정상")
    void createVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        var request = new VocabularyCreateRequest();
        request.setName("testVocabulary");

        // when
        vocabularyWebService.createVocabulary(vookLoginUser, request);

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
        assertThatThrownBy(() -> vocabularyWebService.createVocabulary(vookLoginUser, request))
                .isInstanceOf(VocabularyLimitExceededException.class);
    }

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
        vocabularyWebService.updateVocabulary(vookLoginUser, vocabulary.getUid(), request);

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
        assertThatThrownBy(() -> vocabularyWebService.updateVocabulary(vookLoginUser, "nonExistentUid", request))
                .isInstanceOf(VocabularyNotFoundException.class);
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
        assertThatThrownBy(() -> vocabularyWebService.updateVocabulary(vookLoginUser, vocabulary.getUid(), request))
                .isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("단어장 삭제 - 정상")
    void deleteVocabulary() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        // when
        vocabularyWebService.deleteVocabulary(vookLoginUser, vocabulary.getUid());

        // then
        assertThat(vocabularyRepository.findByUid(vocabulary.getUid())).isEmpty();
    }

    @Test
    @DisplayName("단어장 삭제 - 실패; 해당 단어장이 존재하지 않는 경우")
    void deleteVocabularyError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        testVocabularyCreator.createVocabulary(user);

        // when
        assertThatThrownBy(() -> vocabularyWebService.deleteVocabulary(vookLoginUser, "nonExistentUid"))
                .isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("단어장 삭제 - 실패; 해당 단어장이 다른 사용자의 것인 경우")
    void deleteVocabularyError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        User otherUser = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(otherUser);

        // when
        assertThatThrownBy(() -> vocabularyWebService.deleteVocabulary(vookLoginUser, vocabulary.getUid()))
                .isInstanceOf(VocabularyNotFoundException.class);
    }
}
