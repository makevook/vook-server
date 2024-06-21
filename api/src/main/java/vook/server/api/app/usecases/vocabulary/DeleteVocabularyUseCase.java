package vook.server.api.app.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.VocabularyService;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyDeleteCommand;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public void execute(Command command) {
        User user = userService.findByUid(command.userUid()).orElseThrow();
        vocabularyService.delete(VocabularyDeleteCommand.of(command.vocabularyUid(), user));
    }

    public record Command(
            String userUid,
            String vocabularyUid
    ) {
    }
}
