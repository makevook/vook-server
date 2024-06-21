package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.context.user.domain.User;
import vook.server.api.app.context.vocabulary.application.VocabularyService;
import vook.server.api.app.context.vocabulary.application.data.VocabularyCreateCommand;
import vook.server.api.app.context.vocabulary.domain.Vocabulary;

@Component
@Transactional
@RequiredArgsConstructor
public class TestVocabularyCreator {

    private final VocabularyService vocabularyService;

    public Vocabulary createVocabulary(User user) {
        return vocabularyService.create(VocabularyCreateCommand.of("testVocabulary", user));
    }
}
