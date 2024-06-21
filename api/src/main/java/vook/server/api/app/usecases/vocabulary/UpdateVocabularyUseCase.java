package vook.server.api.app.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.app.contexts.user.application.UserService;
import vook.server.api.app.contexts.user.domain.User;
import vook.server.api.app.contexts.vocabulary.application.VocabularyService;
import vook.server.api.app.contexts.vocabulary.application.data.VocabularyUpdateCommand;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public void execute(Command command) {
        User user = userService.findByUid(command.userUid()).orElseThrow();
        vocabularyService.update(command.toServiceCommand(user));
    }

    public record Command(
            String userUid,
            String vocabularyUid,
            String name
    ) {
        public VocabularyUpdateCommand toServiceCommand(User user) {
            return VocabularyUpdateCommand.of(vocabularyUid(), name(), user);
        }
    }
}
