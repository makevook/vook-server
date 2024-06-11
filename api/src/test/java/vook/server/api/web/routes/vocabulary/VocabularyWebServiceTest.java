package vook.server.api.web.routes.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.vocabulary.VocabularyService;
import vook.server.api.app.vocabulary.exception.VocabularyLimitExceededException;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyCreateRequest;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;

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
    VocabularyService vocabularyService;

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
        List<Vocabulary> vocabularies = vocabularyService.findAllBy(user);
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
}
