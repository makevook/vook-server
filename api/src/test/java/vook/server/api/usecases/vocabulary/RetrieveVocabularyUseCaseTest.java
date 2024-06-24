package vook.server.api.usecases.vocabulary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.common.auth.data.VookLoginUser;
import vook.server.api.web.vocabulary.reqres.VocabularyResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class RetrieveVocabularyUseCaseTest extends IntegrationTestBase {

    @Autowired
    RetrieveVocabularyUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    VocabularyRepository vocabularyRepository;

    @Test
    @DisplayName("용어집 조회 - 정상")
    void vocabularies() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        // when
        var command = new RetrieveVocabularyUseCase.Command(vookLoginUser.getUid());
        var result = useCase.execute(command);
        List<VocabularyResponse> vocabularies = VocabularyResponse.from(result);

        // then
        assertThat(vocabularies).hasSize(1);
        assertThat(vocabularies.getFirst().getUid()).isEqualTo(vocabulary.getUid());
        assertThat(vocabularies.getFirst().getName()).isEqualTo(vocabulary.getName());
        assertThat(vocabularies.getFirst().getCreatedAt()).isEqualTo(vocabulary.getCreatedAt());
    }
}
