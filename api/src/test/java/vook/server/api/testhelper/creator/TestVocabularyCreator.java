package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.term.model.Term;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.vocabulary.model.TermId;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.service.data.VocabularyTermAddCommand;

@Component
@Transactional
@RequiredArgsConstructor
public class TestVocabularyCreator {

    private final VocabularyService vocabularyService;

    public Vocabulary createVocabulary(User user) {
        return vocabularyService.create(VocabularyCreateCommand.of("testVocabulary", new UserId(user.getId())));
    }

    public void addTerm(Vocabulary vocabulary, Term term) {
        vocabularyService.addTerm(VocabularyTermAddCommand.builder()
                .vocabularyUid(vocabulary.getUid())
                .termId(new TermId(term.getId()))
                .build());
    }
}
