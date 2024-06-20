package vook.server.api.web.routes.term;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.domain.term.exception.TermLimitExceededException;
import vook.server.api.app.domain.term.repo.TermRepository;
import vook.server.api.app.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.model.term.Term;
import vook.server.api.model.term.TermSynonym;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.routes.term.reqres.TermCreateRequest;
import vook.server.api.web.routes.term.reqres.TermCreateResponse;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class TermWebServiceTest extends IntegrationTestBase {

    @Autowired
    TermWebService termWebService;

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
    void create() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        TermCreateRequest request = new TermCreateRequest();
        request.setVocabularyUid(vocabulary.getUid());
        request.setTerm("테스트 단어");
        request.setMeaning("테스트 뜻");
        request.setSynonyms(List.of("동의어1", "동의어2"));

        // when
        TermCreateResponse response = termWebService.create(vookLoginUser, request);

        // then
        assertThat(response.getUid()).isNotNull();
        termRepository.findByUid(response.getUid()).ifPresent(term -> {
            assertThat(term.getVocabulary().getUid()).isEqualTo(vocabulary.getUid());
            assertThat(term.getTerm()).isEqualTo(request.getTerm());
            assertThat(term.getMeaning()).isEqualTo(request.getMeaning());
            assertThat(term.getSynonyms().stream().map(TermSynonym::getSynonym))
                    .containsExactlyInAnyOrderElementsOf(request.getSynonyms());
        });
    }

    @Test
    @DisplayName("용어 생성 - 실패; 용어집이 존재하지 않는 경우")
    void createError1() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());

        TermCreateRequest request = new TermCreateRequest();
        request.setVocabularyUid("not-exist");
        request.setTerm("테스트 단어");
        request.setMeaning("테스트 뜻");
        request.setSynonyms(List.of("동의어1", "동의어2"));

        // when
        assertThatThrownBy(() -> termWebService.create(vookLoginUser, request))
                .isInstanceOf(VocabularyNotFoundException.class);
    }

    @Test
    @DisplayName("용어 생성 - 실패; 용어집에 용어를 추가할 수 있는 제한을 초과한 경우 (100개)")
    void createError2() {
        // given
        User user = testUserCreator.createCompletedOnboardingUser();
        VookLoginUser vookLoginUser = VookLoginUser.of(user.getUid());
        Vocabulary vocabulary = testVocabularyCreator.createVocabulary(user);

        termRepository.saveAllAndFlush(
                IntStream.range(0, 100)
                        .mapToObj(i -> Term.forCreateOf(
                                "테스트 단어" + i,
                                "테스트 뜻" + i,
                                vocabulary
                        ))
                        .toList()
        );
        em.clear();

        TermCreateRequest request = new TermCreateRequest();
        request.setVocabularyUid(vocabulary.getUid());
        request.setTerm("테스트 단어");
        request.setMeaning("테스트 뜻");
        request.setSynonyms(List.of("동의어1", "동의어2"));

        // when
        assertThatThrownBy(() -> termWebService.create(vookLoginUser, request))
                .isInstanceOf(TermLimitExceededException.class);
    }
}
