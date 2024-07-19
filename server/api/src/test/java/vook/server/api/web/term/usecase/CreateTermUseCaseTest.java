package vook.server.api.web.term.usecase;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.policy.VocabularyPolicy;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.common.auth.data.VookLoginUser;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class CreateTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    CreateTermUseCase useCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    TermRepository termRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("용어 생성 - 정상")
    void execute() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        var command = new CreateTermUseCase.Command(
                vookLoginUser.getUid(),
                vocabulary.getUid(),
                "테스트 용어",
                "테스트 뜻",
                List.of("동의어1", "동의어2")
        );

        // when
        var result = useCase.execute(command);

        // then
        assertThat(result.uid()).isNotNull();

        Term term = termRepository.findByUid(result.uid()).orElseThrow();
        assertThat(term.getTerm()).isEqualTo(command.term());
        assertThat(term.getMeaning()).isEqualTo(command.meaning());
        assertThat(term.getSynonyms()).containsExactlyInAnyOrderElementsOf(command.synonyms());
        assertThat(term.getVocabulary().termCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("용어 생성 - 정상; 동의어가 없어도 용어 생성이 가능")
    void executeSuccessWithoutSynonyms() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        var command = new CreateTermUseCase.Command(
                vookLoginUser.getUid(),
                vocabulary.getUid(),
                "테스트 용어",
                "테스트 뜻",
                List.of()
        );

        // when
        var result = useCase.execute(command);

        // then
        assertThat(result.uid()).isNotNull();

        Term term = termRepository.findByUid(result.uid()).orElseThrow();
        assertThat(term.getTerm()).isEqualTo(command.term());
        assertThat(term.getMeaning()).isEqualTo(command.meaning());
        assertThat(term.getSynonyms()).isEmpty();
        assertThat(term.getVocabulary().termCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("용어 생성 - 실패; 용어집이 존재하지 않는 경우")
    void executeError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());

        var command = new CreateTermUseCase.Command(
                vookLoginUser.getUid(),
                "not-exist",
                "테스트 용어",
                "테스트 뜻",
                List.of("동의어1", "동의어2")
        );

        // when
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("용어 생성 - 실패; 용어집에 용어를 추가할 수 있는 제한을 초과한 경우 (100개)")
    void executeError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        termRepository.saveAll(
                IntStream.range(0, 100)
                        .mapToObj(i -> Term.forCreateOf(
                                "테스트 용어" + i,
                                "테스트 뜻" + i,
                                List.of(),
                                vocabulary
                        ))
                        .toList()
        );
        em.flush();
        em.clear();

        var command = new CreateTermUseCase.Command(
                vookLoginUser.getUid(),
                vocabulary.getUid(),
                "테스트 용어",
                "테스트 뜻",
                List.of("동의어1", "동의어2")
        );

        // when
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(TermLimitExceededException.class);
    }

    @Test
    @DisplayName("용어 생성 - 실패; 사용자가 용어집 소유자가 아닌 경우")
    void executeError3() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        User anotherUser = testUserCreator.createCompletedOnboardingUser();

        var command = new CreateTermUseCase.Command(
                anotherUser.getUid(),
                vocabulary.getUid(),
                "테스트 용어",
                "테스트 뜻",
                List.of("동의어1", "동의어2")
        );

        // when
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(VocabularyPolicy.NotValidVocabularyOwnerException.class);
    }
}
