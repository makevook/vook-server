package vook.server.api.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.VocabularyUpdateCommand;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

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
