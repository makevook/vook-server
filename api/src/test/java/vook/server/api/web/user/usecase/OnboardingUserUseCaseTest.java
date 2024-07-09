package vook.server.api.web.user.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.Funnel;
import vook.server.api.domain.user.model.Job;
import vook.server.api.domain.user.model.UserRepository;
import vook.server.api.domain.vocabulary.model.TemplateVocabularyType;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestTemplateVocabularyCreator;
import vook.server.api.testhelper.creator.TestUserCreator;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class OnboardingUserUseCaseTest extends IntegrationTestBase {

    @Autowired
    OnboardingUserUseCase useCase;

    @Autowired
    TestTemplateVocabularyCreator testTemplateVocabularyCreator;
    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VocabularyRepository vocabularyRepository;
    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("유저 온보딩 - 정상")
    void onboardingUser() {
        // given
        testTemplateVocabularyCreator.createTemplateVocabulary();
        var user = testUserCreator.createRegisteredUser();
        var command = new OnboardingUserUseCase.Command(user.getUid(), Funnel.FACEBOOK, Job.DEVELOPER);

        // when
        useCase.execute(command);

        // then
        var savedUser = userRepository.findByUid(user.getUid()).orElseThrow();
        assertThat(savedUser.getOnboardingCompleted()).isTrue();

        var vocabularies = vocabularyRepository.findAllByUserUid(new UserUid(savedUser.getUid()));
        assertThat(vocabularies).hasSize(1);
        var vocabulary = vocabularies.getFirst();
        assertThat(vocabulary.getName()).isEqualTo(TemplateVocabularyType.DESIGN.getVocabularyName());
        assertThat(vocabulary.getTerms()).isNotEmpty();
    }
}
