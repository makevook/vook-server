package vook.server.api.web.term.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.vocabulary.model.TermRepository;
import vook.server.api.testhelper.IntegrationTestBase;
import vook.server.api.testhelper.creator.TestUserCreator;
import vook.server.api.testhelper.creator.TestVocabularyCreator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
class BatchDeleteTermUseCaseTest extends IntegrationTestBase {

    @Autowired
    BatchDeleteTermUseCase batchDeleteTermUseCase;

    @Autowired
    TestUserCreator testUserCreator;
    @Autowired
    TestVocabularyCreator testVocabularyCreator;
    @Autowired
    TermRepository termRepository;

    @Test
    @DisplayName("용어 다중 삭제 - 정상")
    void execute() {
        // given
        var user = testUserCreator.createCompletedOnboardingUser();
        var vocabulary = testVocabularyCreator.createVocabulary(user);
        var term1 = testVocabularyCreator.createTerm(vocabulary);
        var term2 = testVocabularyCreator.createTerm(vocabulary);

        // when
        batchDeleteTermUseCase.execute(new BatchDeleteTermUseCase.Command(
                user.getUid(),
                List.of(term1.getUid(), term2.getUid())
        ));

        // then
        assertThat(termRepository.findAll()).isEmpty();
        assertThat(vocabulary.termCount()).isEqualTo(0);
    }

}
