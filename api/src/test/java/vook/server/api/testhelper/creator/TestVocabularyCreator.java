package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.TermService;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.TermCreateCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
@RequiredArgsConstructor
public class TestVocabularyCreator {

    private final VocabularyService vocabularyService;
    private final TermService termService;
    private final AtomicInteger termNameCounter = new AtomicInteger(0);

    public Vocabulary createVocabulary(User user) {
        return vocabularyService.create(VocabularyCreateCommand.of("testVocabulary", new UserId(user.getId())));
    }

    public Term createTerm(Vocabulary vocabulary) {
        int count = termNameCounter.getAndIncrement();
        return createTerm(vocabulary, String.valueOf(count));
    }

    public Term createTerm(Vocabulary vocabulary, String suffix) {
        return termService.create(TermCreateCommand.builder()
                .vocabularyUid(vocabulary.getUid())
                .term("testTerm" + suffix)
                .meaning("testMeaning" + suffix)
                .synonyms(List.of("testSynonym" + suffix))
                .build());
    }
}
