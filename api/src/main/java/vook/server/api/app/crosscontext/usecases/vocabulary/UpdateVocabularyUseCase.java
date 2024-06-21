package vook.server.api.app.crosscontext.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.VocabularyService;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyUpdateCommand;
import vook.server.api.app.contexts.vocabulary.domain.Vocabulary;
import vook.server.api.app.crosscontext.polices.VocabularyPolicy;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final VocabularyPolicy vocabularyPolicy;

    public void execute(Command command) {
        User user = userService.getByUid(command.userUid());

        Vocabulary vocabulary = vocabularyService.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(user, vocabulary);

        vocabularyService.update(command.toServiceCommand());
    }

    public record Command(
            String userUid,
            String vocabularyUid,
            String name
    ) {
        public VocabularyUpdateCommand toServiceCommand() {
            return VocabularyUpdateCommand.of(vocabularyUid, name);
        }
    }
}
