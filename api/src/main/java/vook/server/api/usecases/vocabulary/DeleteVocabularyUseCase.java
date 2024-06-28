package vook.server.api.usecases.vocabulary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.user.model.User;
import vook.server.api.domain.user.service.UserService;
import vook.server.api.domain.vocabulary.model.Vocabulary;
import vook.server.api.domain.vocabulary.service.VocabularyService;
import vook.server.api.usecases.common.polices.VocabularyPolicy;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteVocabularyUseCase {

    private final UserService userService;
    private final VocabularyService vocabularyService;
    private final VocabularyPolicy vocabularyPolicy;

    public void execute(Command command) {
        User user = userService.getCompletedUserByUid(command.userUid());

        Vocabulary vocabulary = vocabularyService.getByUid(command.vocabularyUid());
        vocabularyPolicy.validateOwner(user, vocabulary);

        vocabularyService.delete(command.vocabularyUid());
    }

    public record Command(
            String userUid,
            String vocabularyUid
    ) {
    }
}
