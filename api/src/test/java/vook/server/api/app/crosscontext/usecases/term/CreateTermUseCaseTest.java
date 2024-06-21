package vook.server.api.app.crosscontext.usecases.term;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.term.domain.Term;
import vook.server.api.app.contexts.term.domain.TermRepository;
import vook.server.api.app.contexts.term.domain.TermSynonym;
import vook.server.api.app.contexts.term.domain.VocabularyId;
import vook.server.api.app.contexts.term.exception.TermLimitExceededException;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.contexts.vocabulary.domain.VocabularyRepository;
import vook.server.api.app.contexts.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.auth.data.VookLoginUser;

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
    VocabularyRepository vocabularyRepository;
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
        termRepository.findByUid(result.uid()).ifPresent(term -> {
            assertThat(term.getVocabularyId().getId()).isEqualTo(vocabulary.getId());
            assertThat(term.getTerm()).isEqualTo(command.term());
            assertThat(term.getMeaning()).isEqualTo(command.meaning());
            assertThat(term.getSynonyms().stream().map(TermSynonym::getSynonym))
                    .containsExactlyInAnyOrderElementsOf(command.synonyms());
        });
        int termCount = termRepository.countByVocabularyId(new VocabularyId(vocabulary.getId()));
        assertThat(termCount).isEqualTo(1);
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

        termRepository.saveAllAndFlush(
                IntStream.range(0, 100)
                        .mapToObj(i -> Term.forCreateOf(
                                "테스트 용어" + i,
                                "테스트 뜻" + i,
                                new VocabularyId(vocabulary.getId())
                        ))
                        .toList()
        );
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
}
