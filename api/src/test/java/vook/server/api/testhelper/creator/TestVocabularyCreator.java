package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.context.user.model.User;
import vook.server.api.app.context.vocabulary.VocabularyService;
import vook.server.api.app.context.vocabulary.data.VocabularyCreateCommand;
import vook.server.api.app.context.vocabulary.model.Vocabulary;

@Component
@Transactional
@RequiredArgsConstructor
public class TestVocabularyCreator {

    private final VocabularyService vocabularyService;

    public Vocabulary createVocabulary(User user) {
        return vocabularyService.create(VocabularyCreateCommand.of("testVocabulary", user));
    }
}
