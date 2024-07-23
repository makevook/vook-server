package vook.server.api.web.user.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.template_vocabulary.model.TemplateVocabularyType;
import vook.server.api.domain.user.model.user.UserRepository;
import vook.server.api.domain.user.model.user_info.Funnel;
import vook.server.api.domain.user.model.user_info.Job;
import vook.server.api.domain.vocabulary.model.term.TermRepository;
import vook.server.api.domain.vocabulary.model.vocabulary.UserUid;
import vook.server.api.domain.vocabulary.model.vocabulary.VocabularyRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class OnboardingUserUseCaseTest extends IntegrationTestBase {

    @Autowired
    OnboardingUserUseCase useCase;

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
