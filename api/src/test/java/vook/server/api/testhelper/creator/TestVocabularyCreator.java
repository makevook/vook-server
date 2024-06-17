package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.domain.vocabulary.VocabularyService;
import vook.server.api.app.domain.vocabulary.data.VocabularyCreateCommand;
import vook.server.api.model.user.User;
import vook.server.api.model.vocabulary.Vocabulary;

@Component
@Transactional
@RequiredArgsConstructor
public class TestVocabularyCreator {

    private final VocabularyService vocabularyService;

    public Vocabulary createVocabulary(User user) {
        return vocabularyService.create(VocabularyCreateCommand.of("testVocabulary", user));
    }
}
