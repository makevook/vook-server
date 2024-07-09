package vook.server.api.web.vocabulary.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.logic.UserLogic;
import vook.server.api.domain.vocabulary.logic.VocabularyLogic;
import vook.server.api.domain.vocabulary.logic.dto.VocabularyCreateCommand;
import vook.server.api.domain.vocabulary.model.UserUid;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateVocabularyUseCase {

    private final UserLogic userLogic;
    private final VocabularyLogic vocabularyLogic;

    public void execute(Command command) {
        userLogic.validateCompletedUserByUid(command.userUid());

        vocabularyLogic.create(VocabularyCreateCommand.builder()
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
