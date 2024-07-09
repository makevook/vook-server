package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.logic.TermLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.logic.dto.TermCreateCommand;
import vook.server.api.domain.vocabulary.logic.dto.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.model.Vocabulary;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
@RequiredArgsConstructor
public class TestVocabularyCreator {

    private final VocabularyLogic vocabularyLogic;
    private final TermLogic termLogic;
    private final AtomicInteger vocabularyNameCounter = new AtomicInteger(0);
    private final AtomicInteger termNameCounter = new AtomicInteger(0);

    public Vocabulary createVocabulary(User user) {
        int count = vocabularyNameCounter.getAndIncrement();
        return createVocabulary(user, String.valueOf(count));
    }

    public Vocabulary createVocabulary(User user, String suffix) {
        return vocabularyLogic.create(
                VocabularyCreateCommand.builder()
                        .name("testVocabulary" + suffix)
                        .userUid(new UserUid(user.getUid()))
                        .build()
        );
    }

    public Term createTerm(Vocabulary vocabulary) {
        int count = termNameCounter.getAndIncrement();
        return createTerm(vocabulary, String.valueOf(count));
    }

    public Term createTerm(Vocabulary vocabulary, String suffix) {
        return termLogic.create(TermCreateCommand.builder()
                .vocabularyUid(vocabulary.getUid())
                .term("testTerm" + suffix)
                .meaning("testMeaning" + suffix)
                .synonyms(List.of("testSynonymA" + suffix, "testSynonymB" + suffix))
                .build());
    }
}
