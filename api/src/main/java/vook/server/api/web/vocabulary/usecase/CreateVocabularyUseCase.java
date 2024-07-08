package vook.server.api.web.vocabulary.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.UserUid;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.domain.vocabulary.service.data.VocabularyCreateCommand;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;

    public void execute(Command command) {
        userService.validateCompletedUserByUid(command.userUid());

        vocabularyService.create(VocabularyCreateCommand.builder()
                .name(command.name())
                .userUid(new UserUid(command.userUid()))
                .build()
        );
    }

    public record Command(
            String userUid,
            String name
    ) {
    }
}
