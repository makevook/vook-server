package vook.server.api.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserId;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public void execute(Command command) {
        User user = userService.getByUid(command.userUid());
        vocabularyService.create(VocabularyCreateCommand.of(command.name(), new UserId(user.getId())));
    }

    public record Command(
            String userUid,
            String name
    ) {
    }
}